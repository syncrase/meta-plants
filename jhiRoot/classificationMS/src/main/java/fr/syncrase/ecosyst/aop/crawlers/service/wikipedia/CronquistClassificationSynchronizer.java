package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicUrl;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicCronquistRank.isRangsIntermediaires;

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
//    private Set<AtomicCronquistRank> rangsASupprimer;


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
        @NotNull CronquistClassificationBranch existingClassification, @NotNull CronquistClassificationBranch scrappedClassification
                                 ) throws ClassificationReconstructionException {
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
//            synchronizeUrl(existingClassificationList, offset, i);
        }
    }


//    /**
//     * Mis à jour des IDs des éléments de la première liste pour qu'ils pointent vers les éléments déjà enregistrés en base
//     * Compare chacun des rangs à enregistrer pour les comparer avec la base pour :
//     * <ul>
//     *     <li>mettre à jour les ids</li>
//     *     <li>merger les classifications dans le cas où deux portions de classification distinctes se trouvent être les mêmes</li>
//     * </ul>
//     */

//    private void clearFields() {
////        toInsertClassification = null;
//        rangsASupprimer = null;
//    }

//    /**
//     * <table>
//     *   <thead>
//     *     <tr>
//     *       <th>rankToInsert intermédiaire</th>
//     *       <th>existingRank intermédiaire</th>
//     *       <th>Action</th>
//     *     </tr>
//     *    </thead>
//     *    <tbody>
//     *      <tr>
//     *        <td>non</td>
//     *        <td>non</td>
//     *        <td>Gestion de deux rangs taxonomiques. Si le nom est inconnu, il est ajouté à la liste des noms (synonyme). Sinon le rang est uniquement synchronisé</td>
//     *      </tr>
//     *      <tr>
//     *        <td>non</td>
//     *        <td>oui</td>
//     *        <td>Le rang intermédiaire devient rang taxonomique. Si le rang à ajouter existe déjà, les deux branches de classification sont fusionnées</td>
//     *      </tr>
//     *      <tr>
//     *        <td>oui</td>
//     *        <td>non</td>
//     *        <td>Le rang intermédiaire à ajouter se trouve être un rang taxonomique. Synchronisation du rang</td>
//     *      </tr>
//     *      <tr>
//     *        <td>oui</td>
//     *        <td>oui</td>
//     *        <td>Synchronisation du rang intermédiaire avec la base de données</td>
//     *      </tr>
//     *      <tr>
//     *      </tr>
//     *   </tbody>
//     * </table>
//     *
//     * @param existingClassificationList classification existante mise à plat
//     * @param offset                     Différence de taille des classifications à plat. Correspond au nombre d'éléments de la classification à insérer qui n'est pas encore connu de la base de données
//     * @param i                          Index de l'élément à traiter dans la classification existante
//     */

    private void manageRankConsistency(
        CronquistTaxonomikRanks rankName,
        @NotNull CronquistClassificationBranch existingClassification,
        @NotNull CronquistClassificationBranch scrappedClassification
                                      ) throws ClassificationReconstructionException {

        AtomicCronquistRank existingRank;
        AtomicCronquistRank rankToInsert;
        existingRank = existingClassification.getRang(rankName);
        rankToInsert = scrappedClassification.getRang(rankName);

        boolean lesDeuxRangsSontSignificatifs = !rankToInsert.isRangDeLiaison() && !existingRank.isRangDeLiaison();
        if (lesDeuxRangsSontSignificatifs) {
            if (!rankToInsert.doTheRankHasOneOfTheseNames(existingRank.getNoms())) {// TODO invert
                // Ils sont différents
                // Si le rang que je cherche à insérer existe déjà
                @Nullable AtomicCronquistRank synonym = classificationRepository.findExistingRank(rankToInsert);
                boolean scrappedRankAlreadyExists = synonym != null;
                if (scrappedRankAlreadyExists) {
                    classificationRepository.mergeDeuxRangsSignificatifs(existingClassification, synonym);
                }
                rankToInsert.setId(existingRank.getId());
                rankToInsert.addAllNamesToCronquistRank(existingRank.getNoms());// Ajout des synonymes
            } else {
                // Ils sont égaux
                rankToInsert.setId(existingRank.getId());
                synchronizeNames(existingRank, rankToInsert);
            }
            return;
        }

        boolean uniquementLeRangScrappeEstSignificatif = !rankToInsert.isRangDeLiaison() && existingRank.isRangDeLiaison();
        if (uniquementLeRangScrappeEstSignificatif) {
            @Nullable AtomicCronquistRank rankReplacingTheIntermediateRankList = classificationRepository.findExistingRank(rankToInsert);
            if (rankReplacingTheIntermediateRankList != null) {
                classificationRepository.mergeUnRangSignificatifDansUnRangDeLiaison(existingClassification, rankReplacingTheIntermediateRankList);
            } else {
                // Le rang taxonomique n'existe pas en base → ajoute uniquement l'id du rang intermédiaire qui deviendra alors un rang taxonomique
                rankToInsert.setId(existingRank.getId());
                synchronizeNames(existingRank, rankToInsert);
            }
            return;
        }

        boolean uniquementLeRangExistantEstSignificatif = rankToInsert.isRangDeLiaison() && !existingRank.isRangDeLiaison();
        if (uniquementLeRangExistantEstSignificatif) {
            rankToInsert.setId(existingRank.getId());
            rankToInsert.addAllNamesToCronquistRank(existingRank.getNoms());
            return;
        }

        boolean lesDeuxRangsSontDeLiaison = rankToInsert.isRangDeLiaison() && existingRank.isRangDeLiaison();
        if (lesDeuxRangsSontDeLiaison) {
            rankToInsert.setId(existingRank.getId());
            synchronizeNames(existingRank, rankToInsert);// Pour ne pas créer un nouveau rang de liaison
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
        for (AtomicClassificationNom existingClassificationNom : existingRank.getNoms()) {
            for (AtomicClassificationNom toInsertClassificationNom : rankToInsert.getNoms()) {
                // Ajoute l'id des noms existants
                if (isRangsIntermediaires(existingRank, rankToInsert) || isRangsIntermediaires(existingRank) && !isRangsIntermediaires(rankToInsert) || existingClassificationNom.getNomFr().equals(toInsertClassificationNom.getNomFr())) {
                    toInsertClassificationNom.setId(existingClassificationNom.getId());
                }
            }
            // Ajoute le nom existant s'il n'existe pas dans mes noms à insérer
            if (!rankToInsert.hasThisName(existingClassificationNom.getNomFr())) {
                rankToInsert.addNameToCronquistRank(existingClassificationNom);
            }
        }
    }

    public void applyConsistency(
        @NotNull CronquistClassificationBranch scrappedClassification,
        String urlWiki
                                ) throws ClassificationReconstructionException {


        scrappedClassification.getRangDeBase().addUrls(AtomicUrl.newAtomicUrl(urlWiki));
        CronquistClassificationBranch existingClassification = this.classificationRepository.findExistingPartOfThisClassification(scrappedClassification);

        if (existingClassification != null) {
            makeItConsistent(existingClassification, scrappedClassification);
        }
//        updateFieldsWithExisting();
    }

//    /**
//     * Remplace la portion de classification existante pour qu'elle corresponde à la classification corrigée
//     * depuis le rang intermédiaire existant jusqu'au rang taxonomique suivant
//     * par cette même portion au-dessus du rang taxonomique récupéré
//     * et stocke-les ids des rangs intermédiaires qui ont été déconnectés pour pouvoir les supprimer
//     *
//     * @param existingClassificationList classification existante du rang à insérer comportant un rang intermédiaire qui sera supprimé
//     * @param i                          index de l'élément de classification à partir duquel il faut fusionner les branches
//     * @param offset                     Différence de taille des classifications à plat. Correspond au nombre d'éléments de la classification à insérer qui n'est pas encore connu de la base de données
//     */
//    private void mergeDeuxRangsSignificatifs(@NotNull List<CronquistRank> existingClassificationList, int i, int offset) {
//        CronquistRank mergedRanks = declareAsSynonyms(
//            Objects.requireNonNull(classificationRepository.findExistingRank(toInsertClassification.get(offset + i))),
//            existingClassificationList.get(i)
//        );
//        toInsertClassification.get(offset + i).setId(mergedRanks.getId());
//    }

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

//    public Set<AtomicCronquistRank> getRangsASupprimer() {
//        return rangsASupprimer;
//    }

//    public List<CronquistRank> getToInsertClassification() {
//        return toInsertClassification;
//    }

}
