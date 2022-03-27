package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * À partir de la classification reçue par le constructeur, merge avec la classification existante en base.<br>
 * Gère :
 * <ul>
 *     <li>le merge de deux branches existantes</li>
 *     <li>l'assignation des urls</li>
 *     <li>garanti la synchronisation des IDs</li>
 * </ul>
 */
@Transactional
public class CronquistClassificationSynchronizer {

    private final Logger log = LoggerFactory.getLogger(CronquistClassificationSynchronizer.class);

    private final ClassificationRepository classificationRepository;

    public CronquistClassificationSynchronizer(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }


    /**
     * Mis à jour des IDs des éléments de la première liste pour qu'ils pointent vers les éléments déjà enregistrés en base
     * Compare chacun des rangs à enregistrer pour les comparer avec la base pour :
     * <ul>
     *     <li>mettre à jour les ids</li>
     *     <li>merger les classification dans le cas où deux portions de classification distinctes se trouvent être les mêmes</li>
     * </ul>
     *
     * @param existingClassification
     * @param scrappedClassification
     */
    private void makeItConsistent(
        @NotNull CronquistClassificationBranch existingClassification,
        @NotNull CronquistClassificationBranch scrappedClassification
                                 ) throws ClassificationReconstructionException, UnknownRankId, MoreThanOneResultException {
        Collection<AtomicCronquistRank> existingClassificationList = existingClassification.getClassification();
        if (existingClassificationList.size() == 0) {
            log.info("No existing entity in the database");
            return;
        }
        // Parcours des éléments pour ajouter les IDs des éléments connus
        CronquistTaxonomikRanks[] rangsDisponibles = CronquistTaxonomikRanks.values();
        for (int positionDansLaClassification = existingClassification.ranksCount() - 1; positionDansLaClassification >= 0; positionDansLaClassification--) {
            AtomicCronquistRank existingClassificationRang = existingClassification.getRang(rangsDisponibles[positionDansLaClassification]);
            if (existingClassificationRang == null) {
                continue;
            }
            manageRankConsistency(rangsDisponibles[positionDansLaClassification], existingClassification, scrappedClassification);
        }
    }

    /**
     * <table>
     *   <thead>
     *     <tr>
     *       <th>rankToInsert de liaison</th>
     *       <th>existingRank de liaison</th>
     *       <th>Action</th>
     *     </tr>
     *    </thead>
     *    <tbody>
     *      <tr>
     *        <td>non</td>
     *        <td>non</td>
     *        <td>Gestion de deux rangs significatifs. Si le nom est inconnu, il est ajouté à la liste des noms (synonyme). Sinon le rang est uniquement synchronisé</td>
     *      </tr>
     *      <tr>
     *        <td>non</td>
     *        <td>oui</td>
     *        <td>Le rang de liaison devient rang significatif. Si le rang à ajouter existe déjà, les deux branches de classification sont fusionnées</td>
     *      </tr>
     *      <tr>
     *        <td>oui</td>
     *        <td>non</td>
     *        <td>Le rang de liaison à ajouter se trouve être un rang significatif. Synchronisation du rang</td>
     *      </tr>
     *      <tr>
     *        <td>oui</td>
     *        <td>oui</td>
     *        <td>Synchronisation du rang de liaison avec la base de données</td>
     *      </tr>
     *      <tr>
     *      </tr>
     *   </tbody>
     * </table>
     */
    private void manageRankConsistency(
        CronquistTaxonomikRanks rankName,
        @NotNull CronquistClassificationBranch existingClassification,
        @NotNull CronquistClassificationBranch scrappedClassification
                                      ) throws ClassificationReconstructionException, UnknownRankId, MoreThanOneResultException {

        AtomicCronquistRank existingRank;
        AtomicCronquistRank rankToInsert;
        existingRank = existingClassification.getRang(rankName);
        rankToInsert = scrappedClassification.getRang(rankName);

        boolean lesDeuxRangsSontSignificatifs = !rankToInsert.isRangDeLiaison() && !existingRank.isRangDeLiaison();
        if (lesDeuxRangsSontSignificatifs) {
            mergeDeuxRangsSignificatifs(existingClassification, rankToInsert);
            return;
        }

        boolean uniquementLeRangScrappeEstSignificatif = !rankToInsert.isRangDeLiaison() && existingRank.isRangDeLiaison();
        if (uniquementLeRangScrappeEstSignificatif) {
            mergeUnRangSignificatifDansUnRangDeLiaison(existingClassification, rankToInsert);
            return;
        }

        boolean uniquementLeRangExistantEstSignificatif = rankToInsert.isRangDeLiaison() && !existingRank.isRangDeLiaison();
        if (uniquementLeRangExistantEstSignificatif) {
            rankToInsert.setId(existingRank.getId());
            rankToInsert.addAllNamesToCronquistRank(existingRank.getNoms());
            rankToInsert.addAllUrlsToCronquistRank(existingRank.getUrls());// TODO add test : vérifier que les adresses ne sont pas supprimées
            return;
        }

        boolean lesDeuxRangsSontDeLiaison = rankToInsert.isRangDeLiaison() && existingRank.isRangDeLiaison();
        if (lesDeuxRangsSontDeLiaison) {
            rankToInsert.setId(existingRank.getId());
            synchronizeNames(existingRank, rankToInsert);
        }
    }

    private void mergeUnRangSignificatifDansUnRangDeLiaison(
        @NotNull CronquistClassificationBranch existingClassification,
        @NotNull AtomicCronquistRank rankToInsert
                                                           ) throws ClassificationReconstructionException, UnknownRankId, MoreThanOneResultException {
        AtomicCronquistRank existingRank = existingClassification.getRang(rankToInsert.getRank());
        @Nullable AtomicCronquistRank rankReplacingTheIntermediateRankList = classificationRepository.findExistingRank(rankToInsert);
        if (rankReplacingTheIntermediateRankList != null) {
            classificationRepository.mergeUnRangSignificatifDansUnRangDeLiaison(existingClassification, rankReplacingTheIntermediateRankList);
        } else {
            // Le rang significatif n'existe pas en base → ajoute uniquement l'id du rang de liaison qui deviendra alors un rang significatif
            rankToInsert.setId(existingRank.getId());
            mergeNomScrapeDansLeRangDeLiaisonExistant(existingRank, rankToInsert);
        }
    }

    private void mergeNomScrapeDansLeRangDeLiaisonExistant(
        @NotNull AtomicCronquistRank existingRank,
        @NotNull AtomicCronquistRank rankToInsert
                                                          ) {
        IllegalArgumentException exception = new IllegalArgumentException("Tentative de merger les noms des deux rangs suivants :\n " +
                                                                              "-> (DOIT ETRE UN RANG DE LIAISON) " + existingRank + "\n" +
                                                                              " -> (NE DOIT AVOIR QU'UN NOM) " + rankToInsert);
        AtomicClassificationNom nomDeLiaisonExistant = existingRank.getNoms().stream()
            .filter(atomicClassificationNom -> atomicClassificationNom.getNomFr() == null)
            .findFirst()
            .orElseThrow(() -> exception);
        if (existingRank.isRangDeLiaison() && rankToInsert.getNoms().size() == 1) {
            rankToInsert.getNoms().forEach(atomicClassificationNom -> atomicClassificationNom.setId(nomDeLiaisonExistant.getId()));
        } else {
            throw exception;
        }
    }

    /**
     * Remplace la portion de classification existante pour qu'elle corresponde à la classification corrigée
     * depuis le rang de liaison existant jusqu'au rang significatif suivant
     * par cette même portion au-dessus du rang significatif récupéré
     * et stocke-les ids des rangs de liaisons qui ont été déconnectés pour pouvoir les supprimer
     *
     * @param existingClassification
     * @param rankToInsert
     */
    private void mergeDeuxRangsSignificatifs(
        @NotNull CronquistClassificationBranch existingClassification,
        @NotNull AtomicCronquistRank rankToInsert
                                            ) throws UnknownRankId, ClassificationReconstructionException, MoreThanOneResultException {
        AtomicCronquistRank rankOfExistingClassificationBranch = existingClassification.getRang(rankToInsert.getRank());

        if (rankToInsert.doTheRankHasOneOfTheseNames(rankOfExistingClassificationBranch.getNoms())) {
            // Ils sont égaux
            rankToInsert.setId(rankOfExistingClassificationBranch.getId());
            synchronizeNames(rankOfExistingClassificationBranch, rankToInsert);
        } else {
            // Ils sont différents
            // Si le rang que je cherche à insérer existe déjà
            @Nullable AtomicCronquistRank synonym = classificationRepository.findExistingRank(rankToInsert);
            boolean scrappedRankAlreadyExists = synonym != null;
            if (scrappedRankAlreadyExists) {
//                classificationRepository.mergeDeuxRangsSignificatifs(existingClassification, synonym);
                classificationRepository.mergeUnRangSignificatifDansUnRangDeLiaison(existingClassification, synonym);
            }
            rankToInsert.setId(rankOfExistingClassificationBranch.getId());
//            rankToInsert.addAllNamesToCronquistRank(rankOfExistingClassificationBranch.getNoms());// Ajout des synonymes
            synchronizeNames(rankOfExistingClassificationBranch, rankToInsert);
        }
    }

    /**
     * Synchronise le nouveau rang avec le rang existant. Ajoute-les ids aux noms connus que je souhaite enregistrer, ajoute l'ensemble des noms connu absent du rang à enregistrer
     *
     * @param existingRank rang existant
     * @param rankToInsert nouveau rang à insérer
     */
    private void synchronizeNames(
        @NotNull AtomicCronquistRank existingRank,
        AtomicCronquistRank rankToInsert
                                 ) {

        // Les deux rangs sont sémantiquement les mêmes
        // Si le nom à insérer existe => ajout de l'id au rang à insérer
        existingRank.getNoms().forEach(existingNom -> {
            boolean leNomAInsererExisteDeja = rankToInsert.getNoms().stream()
                .map(AtomicClassificationNom::getNomFr)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .contains(existingNom.getNomFr());
            //
            if (leNomAInsererExisteDeja) {
                rankToInsert.getNoms().forEach(toInsertNom -> {
                    if (toInsertNom.getNomFr().contentEquals(existingNom.getNomFr())) {
                        toInsertNom.setId(existingNom.getId());
                    }
                });
            }

            boolean cestUnRangDeLiaison = rankToInsert.isRangDeLiaison() && existingRank.isRangDeLiaison();
            if (cestUnRangDeLiaison) {
                rankToInsert.getNoms().forEach(toInsertNom -> {
                    toInsertNom.setId(existingNom.getId());
                });
            }

            if (!leNomAInsererExisteDeja && !cestUnRangDeLiaison) {
                rankToInsert.getNoms().add(existingNom);
            }

        });
    }

    public void applyConsistency(
        @NotNull CronquistClassificationBranch scrappedClassification, String urlWiki
                                ) throws ClassificationReconstructionException, UnknownRankId, MoreThanOneResultException {

        scrappedClassification.getRangDeBase().addUrls(AtomicUrl.newAtomicUrl(urlWiki));
        scrappedClassification.inferAllRank();

        CronquistClassificationBranch existingClassification = this.classificationRepository.findExistingPartOfThisClassification(scrappedClassification);

        if (existingClassification != null) {
            makeItConsistent(existingClassification, scrappedClassification);
        }
    }

    //    private void synchronizeUrl(@NotNull List<CronquistRank> existingClassificationList, int offset, int i) {
    //        CronquistRank existingRank, rankToInsert;
    //        existingRank = existingClassificationList.get(i);
    //        rankToInsert = toInsertClassification.get(offset + i);
    //
    //        if (existingRank.getUrls().size() > 0) {
    //            if (rankToInsert.getUrls().size() > 0) {// Must be = 1. Check > 1 and throw error?
    //                if (rankToInsert.getUrls().size() > 1) {
    //                    log.warn("Attention: {} urls trouvée. Correction nécessaire", existingRank.getUrls().size());
    //                }
    //                Url urlsAInserer = new ArrayList<>(rankToInsert.getUrls()).get(0);
    //                Optional<Url> matchingExistingUrl = existingRank.getUrls().stream()
    //                    .filter(url -> url.getUrl().equals(urlsAInserer.getUrl()))
    //                    .findFirst();
    //                matchingExistingUrl.ifPresent(value -> rankToInsert.getUrls().forEach(url -> url.setId(value.getId())));
    //            }
    //            rankToInsert.addAllUrlsToCronquistRank(existingRank.getUrls());
    //        }
    //    }

}
