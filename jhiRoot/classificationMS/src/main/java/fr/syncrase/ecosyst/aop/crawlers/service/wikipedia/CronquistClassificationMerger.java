package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.service.ClassificationNomQueryService;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.criteria.ClassificationNomCriteria;
import fr.syncrase.ecosyst.service.criteria.CronquistRankCriteria;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistUtils.*;

/**
 * À partir de la classification reçue par le constructeur, merge avec la classification existante en base.<br>
 * Gère :
 * <ul>
 *     <li>le merge de deux branches existantes</li>
 *     <li>l'assignation des urls</li>
 *     <li>garanti la synchronisation des IDs</li>
 * </ul>
 */
public class CronquistClassificationMerger {

    private final Logger log = LoggerFactory.getLogger(CronquistClassificationMerger.class);

    private final CronquistRankQueryService cronquistRankQueryService;
    private final MergeResult persistanceResult;
    private final ClassificationNomQueryService classificationNomQueryService;
    private CronquistClassification existingClassification;


    public CronquistClassificationMerger(@NotNull CronquistClassification cronquistClassification, String urlWiki, CronquistRankQueryService cronquistRankQueryService, ClassificationNomQueryService classificationNomQueryService) {
        this.cronquistRankQueryService = cronquistRankQueryService;
        this.classificationNomQueryService = classificationNomQueryService;
        persistanceResult = new MergeResult();
        getTheDeepestExistingRankInTheClassification(cronquistClassification, urlWiki);

    }

    private void getTheDeepestExistingRankInTheClassification(@NotNull CronquistClassification cronquistClassification, String urlWiki) {
        cronquistClassification.clearTail();
        persistanceResult.list = cronquistClassification.getClassificationAscendante();
        persistanceResult.list.get(0).addUrls(new Url().url(urlWiki));
        // Parcours du plus bas rang jusqu'au plus élevé pour trouver le premier rang existant en base
        for (CronquistRank cronquistRank : persistanceResult.list) {
            // Si je n'ai toujours pas récupéré de rang connu, je regarde encore pour ce nouveau rang
            List<CronquistRank> existing = findExistingRank(cronquistRank);
            // Si je viens d'en trouver un
            if (existing != null && existing.size() != 0) {
                // À partir de ce moment-là, il n'est plus nécessaire d'interroger la base.
                // On a obtenu l'arborescence ascendante complète du rang taxonomique
                if (existing.size() > 1) {
                    log.error("Plus d'un rang existant à été trouvé. Un rang doit être unique. Erreur");
                }
                existingClassification = new CronquistClassification(existing.get(0));
                break;
            }
        }
    }

    /**
     * @param cronquistRank Rang dont il faut vérifier la présence en base
     * @return La liste des rangs qui correspondent en base. Soit une liste vide, soit une liste d'un unique élément
     */
    private List<CronquistRank> findExistingRank(@NotNull CronquistRank cronquistRank) {
        if (isRangIntermediaire(cronquistRank) && cronquistRank.getId() == null) {
            return new ArrayList<>();
        }

        updateNomsIds(cronquistRank);

        // Pas de nom => rang inconnu en base
        if (cronquistRank.getNoms().stream().anyMatch(classificationNom -> classificationNom.getId() != null)) {
            return fetchExistingRank(cronquistRank);
        }
        return new ArrayList<>();
    }

    private List<CronquistRank> fetchExistingRank(@NotNull CronquistRank cronquistRank) {
        LongFilter nomFilter = new LongFilter();
        nomFilter.setIn(cronquistRank.getNoms().stream().map(ClassificationNom::getId).collect(Collectors.toList()));

        CronquistRankCriteria.CronquistTaxonomikRanksFilter rankFilter = new CronquistRankCriteria.CronquistTaxonomikRanksFilter();
        rankFilter.setEquals(cronquistRank.getRank());

        CronquistRankCriteria rankCrit = new CronquistRankCriteria();
        rankCrit.setNomsId(nomFilter);
        rankCrit.setRank(rankFilter);
        return cronquistRankQueryService.findByCriteria(rankCrit);
    }

    private void updateNomsIds(@NotNull CronquistRank cronquistRank) {
        Iterator<ClassificationNom> nomIterator = cronquistRank.getNoms().iterator();
        ClassificationNom nom;
        while (nomIterator.hasNext()) {
            nom = nomIterator.next();
            StringFilter classifNomFilter = new StringFilter();
            classifNomFilter.setEquals(nom.getNomFr());

            ClassificationNomCriteria classificationNomCriteria = new ClassificationNomCriteria();
            classificationNomCriteria.setNomFr(classifNomFilter);
            List<ClassificationNom> classificationNoms = classificationNomQueryService.findByCriteria(classificationNomCriteria);
            if (classificationNoms.size() > 0) {// Soit inexistant, soit un unique résultat (unique constraint)
                nom.setId(classificationNoms.get(0).getId());
            }
        }
    }

    /**
     * Compare chacun des rangs à enregistrer pour les comparer avec la base pour :
     * <ul>
     *     <li>mettre à jour les ids</li>
     *     <li>merger les classification dans le cas où deux portions de classification distinctes se trouvent être les mêmes</li>
     * </ul>
     *
     * @return la classification synchronisée avec la base et les ids des rangs obsolètes après merge des sections de classification
     */
    public MergeResult synchronizeClassifications() {
        updateFieldsWithExisting(existingClassification != null ? existingClassification.getClassificationAscendante() : new ArrayList<>());
        return persistanceResult;
    }

    /**
     * Mis à jour des IDs des éléments de la première liste pour qu'ils pointent vers les éléments déjà enregistrés en base
     *
     * @param existingClassificationList Liste des rangs de l'arborescence existante en base de données
     */
    private void updateFieldsWithExisting(@NotNull List<CronquistRank> existingClassificationList) {
        if (existingClassificationList.size() == 0) {
            log.info("No existing entity in the database");
            return;
        }
        int offset = persistanceResult.list.size() - existingClassificationList.size();
        if (!doTheRankHasOneOfTheseNames(persistanceResult.list.get(offset), existingClassificationList.get(0).getNoms())) {
            log.error("At the depth of the first known rank, ranks names must be equals");
            return;
        }
        // Parcours des éléments pour ajouter les IDs des éléments connus
        for (int i = 0; i < existingClassificationList.size(); i++) {
            updateNameAndId(existingClassificationList, offset, i);
            synchronizeUrl(existingClassificationList, offset, i);
        }
    }

    /**
     * <table>
     *   <thead>
     *     <tr>
     *       <th>rankToInsert intermédiaire</th>
     *       <th>existingRank intermédiaire</th>
     *       <th>Action</th>
     *     </tr>
     *    </thead>
     *    <tbody>
     *      <tr>
     *        <td>non</td>
     *        <td>non</td>
     *        <td>Gestion de deux rangs taxonomiques. Si le nom est inconnu, il est ajouté à la liste des noms (synonyme). Sinon le rang est uniquement synchronisé</td>
     *      </tr>
     *      <tr>
     *        <td>non</td>
     *        <td>oui</td>
     *        <td>Le rang intermédiaire devient rang taxonomique. Si le rang à ajouter existe déjà, les deux branches de classification sont fusionnées</td>
     *      </tr>
     *      <tr>
     *        <td>oui</td>
     *        <td>non</td>
     *        <td>Le rang intermédiaire à ajouter se trouve être un rang taxonomique. Synchronisation du rang</td>
     *      </tr>
     *      <tr>
     *        <td>oui</td>
     *        <td>oui</td>
     *        <td>Synchronisation du rang intermédiaire avec la base de données</td>
     *      </tr>
     *      <tr>
     *      </tr>
     *   </tbody>
     * </table>
     *
     * @param existingClassificationList classification existante mise à plat
     * @param offset                     Différence de taille des classifications à plat. Correspond au nombre d'éléments de la classification à insérer qui n'est pas encore connu de la base de données
     * @param i                          Index de l'élément à traiter dans la classification existante
     */
    private void updateNameAndId(@NotNull List<CronquistRank> existingClassificationList, int offset, int i) {
        CronquistRank existingRank, rankToInsert;
        existingRank = existingClassificationList.get(i);
        rankToInsert = persistanceResult.list.get(offset + i);

        if (!isRangIntermediaire(rankToInsert) && !isRangIntermediaire(existingRank)) {
            if (!doTheRankHasOneOfTheseNames(rankToInsert, existingRank.getNoms())) {
                // Ils sont différents
                rankToInsert.setId(existingRank.getId());
                addAllNamesToCronquistRank(rankToInsert, existingRank.getNoms());// Ajout des synonymes
            } else {
                // Ils sont égaux
                rankToInsert.setId(existingRank.getId());
                synchronizeNames(existingRank, rankToInsert);
            }
            return;
        }
        if (!isRangIntermediaire(rankToInsert) && isRangIntermediaire(existingRank)) {
            List<CronquistRank> rankReplacingTheIntermediateRankList = findExistingRank(rankToInsert);
            if (rankReplacingTheIntermediateRankList.size() > 0) {// Must be = 1. Check if > 1 and throw error ?
                mergeTheTwoClassificationBranches(existingClassificationList, i, rankToInsert, rankReplacingTheIntermediateRankList);
            } else {
                // Le rang taxonomique n'existe pas en base → ajoute uniquement l'id du rang intermédiaire qui deviendra alors un rang taxonomique
                rankToInsert.setId(existingRank.getId());
            }
            return;
        }
        if (isRangIntermediaire(rankToInsert) && !isRangIntermediaire(existingRank)) {
            rankToInsert.setId(existingRank.getId());
            addAllNamesToCronquistRank(rankToInsert, existingRank.getNoms());
            return;
        }
        if (isRangIntermediaire(rankToInsert) && isRangIntermediaire(existingRank)) {
            rankToInsert.setId(existingRank.getId());
            synchronizeNames(existingRank, rankToInsert);
        }
    }

    /**
     * Synchronise le nouveau rang avec le rang existant. Ajoute-les ids aux noms connus que je souhaite enregistrer, ajoute l'ensemble des noms connu absent du rang à enregistrer
     *
     * @param existingRank rang existant
     * @param rankToInsert nouveau rang à insérer
     */
    private void synchronizeNames(@NotNull CronquistRank existingRank, CronquistRank rankToInsert) {
        for (ClassificationNom existingClassificationNom : existingRank.getNoms()) {
            for (ClassificationNom toInsertClassificationNom : rankToInsert.getNoms()) {
                // Ajoute l'id des noms existants
                if (isRangsIntermediaires(existingRank, rankToInsert) || existingClassificationNom.getNomFr().equals(toInsertClassificationNom.getNomFr())) {
                    toInsertClassificationNom.setId(existingClassificationNom.getId());
                }
            }
            // Ajoute le nom existant s'il n'existe pas dans mes noms à insérer
            if (!hasThisName(rankToInsert, existingClassificationNom.getNomFr())) {
                addNameToCronquistRank(rankToInsert, existingClassificationNom);
            }
        }
    }

    /**
     * Remplace la portion de classification ascendante
     * depuis le rang intermédiaire existant jusqu'au rang taxonomique suivant
     * par cette même portion au-dessus du rang taxonomique récupéré
     * et stocke-les ids des rangs intermédiaires qui ont été déconnectés pour pouvoir les supprimer
     *
     * @param existingClassificationList           classification existante du rang à insérer comportant un rang intermédiaire qui sera supprimé
     * @param i                                    index de l'élément de classification à partir duquel il faut fusionner les branches
     * @param rankToInsert                         nouveau rang à insérer
     * @param rankReplacingTheIntermediateRankList rang taxonomique existant en base qui sera fusionné avec le rang intermédiaire
     */
    private void mergeTheTwoClassificationBranches(@NotNull List<CronquistRank> existingClassificationList, int i, @NotNull CronquistRank rankToInsert, @NotNull List<CronquistRank> rankReplacingTheIntermediateRankList) {
        CronquistRank rankReplacingTheIntermediateRank = rankReplacingTheIntermediateRankList.get(0);
        rankToInsert.setId(rankReplacingTheIntermediateRank.getId());
        rankToInsert.getParent().setId(rankReplacingTheIntermediateRank.getParent().getId());
        CronquistClassification cronquistClassification = new CronquistClassification(rankReplacingTheIntermediateRank);
        List<CronquistRank> classificationReverseList = cronquistClassification.getClassificationAscendante();
        for (int index = i; index < existingClassificationList.size(); index++) {
            if (existingClassificationList.get(index).equals(classificationReverseList.get(index - i))) {
                break;
            }
            persistanceResult.rangsIntermediairesASupprimer.add(existingClassificationList.get(index));
            existingClassificationList.set(index, classificationReverseList.get(index - i));
        }
    }

    private void synchronizeUrl(@NotNull List<CronquistRank> existingClassificationList, int offset, int i) {
        CronquistRank existingRank, rankToInsert;
        existingRank = existingClassificationList.get(i);
        rankToInsert = persistanceResult.list.get(offset + i);

        if (existingRank.getUrls().size() > 0) {
            if (rankToInsert.getUrls().size() > 0) {// Must be = 1. Check > 1 and throw error?
                Url urlsAInserer = new ArrayList<>(rankToInsert.getUrls()).get(0);
                Optional<Url> matchingExistingUrl = existingRank.getUrls().stream()
                    .filter(url -> url.getUrl().equals(urlsAInserer.getUrl()))
                    .findFirst();
                matchingExistingUrl.ifPresent(value -> rankToInsert.getUrls().forEach(url -> url.setId(value.getId())));
            }
            addAllUrlsToCronquistRank(rankToInsert, existingRank.getUrls());
        }
    }

    static class MergeResult {

        List<CronquistRank> rangsIntermediairesASupprimer;
        List<CronquistRank> list;

        public MergeResult() {
            rangsIntermediairesASupprimer = new ArrayList<>();
        }
    }
}
