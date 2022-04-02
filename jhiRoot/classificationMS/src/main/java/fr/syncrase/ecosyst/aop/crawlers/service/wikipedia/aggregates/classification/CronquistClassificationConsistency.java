package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.ClassificationReconstructionException;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.InconsistentRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.MoreThanOneResultException;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.UnknownRankId;
import fr.syncrase.ecosyst.domain.IClassificationNom;
import fr.syncrase.ecosyst.domain.ICronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
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
public class CronquistClassificationConsistency {

    private final Logger log = LoggerFactory.getLogger(CronquistClassificationConsistency.class);

    private final ClassificationRepository classificationRepository;

    public CronquistClassificationConsistency(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }


    /**
     * Parcours la classification pour rendre l'existant cohérent avec le rang à insérer
     *
     * @param existingClassification une branche de classification existante en base de données
     * @param scrappedClassification classification scrappée devant être inséré en base de données
     * @return Une nouvelle ClassificationBranch construite à partir de la classification scrapé puis rendue cohérente avec les données déjà existantes en base
     */
    private @NotNull CronquistClassificationBranch makeItConsistent(
        @NotNull CronquistClassificationBranch existingClassification,
        @NotNull CronquistClassificationBranch scrappedClassification
                                                                   ) throws UnknownRankId, MoreThanOneResultException, InconsistentRank, ClassificationReconstructionException {
        Collection<ICronquistRank> existingClassificationList = existingClassification.getClassification();
        if (existingClassificationList.size() == 0) {
            log.info("No existing entity in the database");
            return scrappedClassification;
        }
        CronquistClassificationBranch consistentClassification = scrappedClassification.clone();
        // Parcours des éléments pour ajouter les IDs des éléments connus
        RankName[] rangsDisponibles = RankName.values();
        int positionDansLaClassification = existingClassification.ranksCount() - 1;
        while (positionDansLaClassification >= 0) {
            RankName currentRankName = rangsDisponibles[positionDansLaClassification];
            ICronquistRank existingClassificationRang = existingClassification.getRang(currentRankName);
            if (existingClassificationRang == null) {
                positionDansLaClassification--;
                continue;
            }
            ICronquistRank consistentRank = manageRankConsistency(currentRankName, existingClassification, scrappedClassification);
            consistentClassification.put(currentRankName, consistentRank);
            positionDansLaClassification--;
        }
        consistentClassification.clearTail();
        return consistentClassification;
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
     *
     * @return Le rang qui est cohérent avec les données de classification déjà enregistrées
     */
    private @NotNull ICronquistRank manageRankConsistency(
        RankName rankName,
        @NotNull CronquistClassificationBranch existingClassification,
        @NotNull CronquistClassificationBranch scrappedClassification
                                                         ) throws UnknownRankId, MoreThanOneResultException, InconsistentRank, ClassificationReconstructionException {

        ICronquistRank existingRank;
        ICronquistRank rankToInsert;
        existingRank = existingClassification.getRang(rankName);
        rankToInsert = scrappedClassification.getRang(rankName);

        boolean lesDeuxRangsSontSignificatifs = !rankToInsert.isRangDeLiaison() && !existingRank.isRangDeLiaison();
        boolean uniquementLeRangScrappeEstSignificatif = !rankToInsert.isRangDeLiaison() && existingRank.isRangDeLiaison();
        if (lesDeuxRangsSontSignificatifs || uniquementLeRangScrappeEstSignificatif) {
            return merge(existingClassification, rankToInsert);
        }

        boolean uniquementLeRangExistantEstSignificatif = rankToInsert.isRangDeLiaison() && !existingRank.isRangDeLiaison();
        if (uniquementLeRangExistantEstSignificatif) {
            rankToInsert.setId(existingRank.getId());
            rankToInsert.addAllNamesToCronquistRank(existingRank.getNoms());
            rankToInsert.addAllUrlsToCronquistRank(existingRank.getUrls());// TODO add test : vérifier que les adresses ne sont pas supprimées
            return rankToInsert;
        }

        boolean lesDeuxRangsSontDeLiaison = rankToInsert.isRangDeLiaison() && existingRank.isRangDeLiaison();
        if (lesDeuxRangsSontDeLiaison) {
            rankToInsert.setId(existingRank.getId());
            //            rankToInsert.addAllNamesToCronquistRank();
            synchronizeNames(existingRank, rankToInsert);
        }
        return rankToInsert;
    }

    /**
     * Met à jour la classification existante pour qu'elle corresponde puisse accueillir la classification scrappée
     *
     * @param existingClassification une branche de classification existante en base de données
     * @param rankToInsert           rang à insérer devant être intégrée à la classification existante
     * @return Le rang qui résulte du merge
     */
    private @NotNull ICronquistRank merge(
        @NotNull CronquistClassificationBranch existingClassification,
        @NotNull ICronquistRank rankToInsert
                                         ) throws UnknownRankId, MoreThanOneResultException, InconsistentRank, ClassificationReconstructionException {
        // TODO quand je merge le rang significatif dans le rang de liaison je dois supprimer le nom de liaison du rang de liaison
        ICronquistRank existingRank = existingClassification.getRang(rankToInsert.getRankName());
        boolean leRangAInsererNEstPasDansLaClassificationExistante = existingRank.isRangSignificatif() && !rankToInsert.doTheRankHasOneOfTheseNames(existingRank.getNoms());
        @Nullable ICronquistRank synonym = classificationRepository.findExistingRank(rankToInsert);

        ICronquistRank consistentRank = existingRank;
        if ((leRangAInsererNEstPasDansLaClassificationExistante || existingRank.isRangDeLiaison()) && synonym != null) {
            consistentRank = classificationRepository.merge(existingClassification, synonym);// TODO supprimer les side-effects, utiliser un retour
        }
        rankToInsert.setId(consistentRank.getId());
        synchronizeNames(consistentRank, rankToInsert);
        return rankToInsert;
    }

    /**
     * Synchronise le nouveau rang avec le rang existant. Ajoute-les ids aux noms connus que je souhaite enregistrer, ajoute l'ensemble des noms connu absent du rang à enregistrer
     *
     * @param existingRank rang existant
     * @param rankToInsert nouveau rang à insérer
     */
    private void synchronizeNames(
        @NotNull ICronquistRank existingRank,
        @NotNull ICronquistRank rankToInsert
                                 ) {
        int size = rankToInsert.getNoms().size();
        assert size == 1;

        for (IClassificationNom existingNom : existingRank.getNoms()) {
            boolean leNomSignificatifAInsererExisteDeja = rankToInsert.getNoms().stream()
                .map(IClassificationNom::getNomFr)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .contains(existingNom.getNomFr());

            if (leNomSignificatifAInsererExisteDeja) {
                rankToInsert.getNoms().forEach(toInsertNom -> {
                    if (toInsertNom.getNomFr().contentEquals(existingNom.getNomFr())) {
                        toInsertNom.setId(existingNom.getId());
                    }
                });
                continue;
            }

            if (existingRank.isRangDeLiaison()) {
                rankToInsert.getNoms().forEach(toInsertNom -> toInsertNom.setId(existingNom.getId()));
                continue;
            }

            // C'est un rang de liaison dans lequel j'ajoute des noms significatifs
            rankToInsert.addNameToCronquistRank(existingNom);
        }
    }

    public @NotNull CronquistClassificationBranch getConsistentClassification(
        @NotNull CronquistClassificationBranch scrappedClassification
                                                                             ) throws ClassificationReconstructionException, UnknownRankId, MoreThanOneResultException, InconsistentRank {
        scrappedClassification.inferAllRank();
        CronquistClassificationBranch existingClassification = this.classificationRepository.findExistingPartOfThisClassification(scrappedClassification);
        if (existingClassification != null) {
            return makeItConsistent(existingClassification, scrappedClassification);
        }
        return scrappedClassification;
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
