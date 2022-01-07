package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.service.ClassificationNomQueryService;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.UrlQueryService;
import fr.syncrase.ecosyst.service.criteria.ClassificationNomCriteria;
import fr.syncrase.ecosyst.service.criteria.CronquistRankCriteria;
import fr.syncrase.ecosyst.service.criteria.UrlCriteria;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.util.*;
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
@Transactional
public class CronquistClassificationSynchronizer {

    private final Logger log = LoggerFactory.getLogger(CronquistClassificationSynchronizer.class);

    private final CronquistRankQueryService cronquistRankQueryService;
    private final ClassificationNomQueryService classificationNomQueryService;
    private final Set<CronquistRank> rangsIntermediairesASupprimer;
    private CronquistClassification existingClassification;
    private List<CronquistRank> toInsertClassification;
    private final UrlQueryService urlQueryService;


    public CronquistClassificationSynchronizer(@NotNull CronquistClassification cronquistClassification, String urlWiki, CronquistRankQueryService cronquistRankQueryService, ClassificationNomQueryService classificationNomQueryService, UrlQueryService urlQueryService) {
        this.cronquistRankQueryService = cronquistRankQueryService;
        this.classificationNomQueryService = classificationNomQueryService;
        this.urlQueryService = urlQueryService;
        rangsIntermediairesASupprimer = new HashSet<>();
        setExistingClassification(cronquistClassification, urlWiki);
        synchronizeClassifications();
    }

    /**
     * Récupère le rang connu le plus profond de la classification
     *
     * @param cronquistClassification La classification de cronquist aux éléments imbriqués
     * @param urlWiki                 L'url du wiki d'où a été extraite la classification
     */
    private void setExistingClassification(@NotNull CronquistClassification cronquistClassification, String urlWiki) {
        cronquistClassification.clearTail();
        toInsertClassification = cronquistClassification.getClassificationAscendante();
        toInsertClassification.get(0).addUrls(new Url().url(urlWiki));
        // Parcours du plus bas rang jusqu'au plus élevé pour trouver le premier rang existant en base
        for (CronquistRank cronquistRank : toInsertClassification) {
            // Si je n'ai toujours pas récupéré de rang connu, je regarde encore pour ce nouveau rang
            CronquistRank existing = findExistingRank(cronquistRank);
            // Si je viens d'en trouver un
            if (existing != null) {
                existingClassification = new CronquistClassification(existing);
                // À partir de ce moment-là, il n'est plus nécessaire d'interroger la base.
                // On a obtenu l'arborescence ascendante complète du rang taxonomique
                break;
            }
        }
    }

    /**
     * @param cronquistRank Rang dont il faut vérifier la présence en base
     * @return Le rang qui correspond. Null si le rang n'existe pas
     */
    private @Nullable CronquistRank findExistingRank(@NotNull CronquistRank cronquistRank) {
        if (isRangIntermediaire(cronquistRank) && cronquistRank.getId() == null) {
            return null;
        }
        updateNomsIds(cronquistRank);
        // Pas de nom => rang inconnu en base
        if (cronquistRank.getNoms().stream().anyMatch(classificationNom -> classificationNom.getId() != null)) {
            return fetchExistingRank(cronquistRank);
        }
        return null;
    }

    private CronquistRank fetchExistingRank(@NotNull CronquistRank cronquistRank) {
        LongFilter nomFilter = new LongFilter();
        nomFilter.setIn(cronquistRank.getNoms().stream().map(ClassificationNom::getId).collect(Collectors.toList()));

        CronquistRankCriteria.CronquistTaxonomikRanksFilter rankFilter = new CronquistRankCriteria.CronquistTaxonomikRanksFilter();
        rankFilter.setEquals(cronquistRank.getRank());

        CronquistRankCriteria rankCrit = new CronquistRankCriteria();
        rankCrit.setNomsId(nomFilter);
        rankCrit.setRank(rankFilter);
        CronquistRank cronquistRankByCriteria = cronquistRankQueryService.findByCriteria(rankCrit).get(0);// Un nom existe, ce rang existe obligatoirement (DB constraint)
        addAllNames(cronquistRankByCriteria);
        addAllUrls(cronquistRankByCriteria);
        return cronquistRankByCriteria;
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

    private void addAllNames(@NotNull CronquistRank cronquistRank) {
        if (cronquistRank.getNoms().stream().anyMatch(classificationNom -> classificationNom.getId() == null)) {
            cronquistRank.getNoms().removeIf(classificationNom -> classificationNom.getId() == null);
            LongFilter idFilter = new LongFilter();
            idFilter.setEquals(cronquistRank.getId());
            ClassificationNomCriteria classificationNomCriteria = new ClassificationNomCriteria();
            classificationNomCriteria.setCronquistRankId(idFilter);

            List<ClassificationNom> classificationNoms = classificationNomQueryService.findByCriteria(classificationNomCriteria);
            HashSet<ClassificationNom> classificationNoms1 = new HashSet<>(classificationNoms);
            cronquistRank.getNoms().addAll(classificationNoms1);
        }
    }

    private void addAllUrls(@NotNull CronquistRank cronquistRank) {
        if (cronquistRank.getUrls().stream().anyMatch(url -> url.getId() == null)) {
            cronquistRank.getUrls().removeIf(url -> url.getId() == null);
            LongFilter idFilter = new LongFilter();
            idFilter.setEquals(cronquistRank.getId());
            UrlCriteria urlCriteria = new UrlCriteria();
            urlCriteria.setCronquistRankId(idFilter);

            List<Url> urlByCriteria = urlQueryService.findByCriteria(urlCriteria);
            HashSet<Url> urls = new HashSet<>(urlByCriteria);
            cronquistRank.getUrls().addAll(urls);
        }
    }

    /**
     * Compare chacun des rangs à enregistrer pour les comparer avec la base pour :
     * <ul>
     *     <li>mettre à jour les ids</li>
     *     <li>merger les classification dans le cas où deux portions de classification distinctes se trouvent être les mêmes</li>
     * </ul>
     */
    public void synchronizeClassifications() {
        updateFieldsWithExisting(existingClassification != null ? existingClassification.getClassificationAscendante() : new ArrayList<>());
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
        int offset = toInsertClassification.size() - existingClassificationList.size();
        if (!doTheRankHasOneOfTheseNames(toInsertClassification.get(offset), existingClassificationList.get(0).getNoms())) {
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
        rankToInsert = toInsertClassification.get(offset + i);

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
            @Nullable CronquistRank rankReplacingTheIntermediateRankList = findExistingRank(rankToInsert);
            if (rankReplacingTheIntermediateRankList != null) {// Must be = 1. Check if > 1 and throw error ?
                mergeTheTwoClassificationBranches(existingClassificationList, i, offset);
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
     * Remplace la portion de classification existante pour qu'elle corresponde à la classification corrigée
     * depuis le rang intermédiaire existant jusqu'au rang taxonomique suivant
     * par cette même portion au-dessus du rang taxonomique récupéré
     * et stocke-les ids des rangs intermédiaires qui ont été déconnectés pour pouvoir les supprimer
     *
     * @param existingClassificationList classification existante du rang à insérer comportant un rang intermédiaire qui sera supprimé
     * @param i                          index de l'élément de classification à partir duquel il faut fusionner les branches
     * @param offset                     Différence de taille des classifications à plat. Correspond au nombre d'éléments de la classification à insérer qui n'est pas encore connu de la base de données
     */
    private void mergeTheTwoClassificationBranches(@NotNull List<CronquistRank> existingClassificationList, int i, int offset) {

        List<CronquistRank> toBeMergedClassificationList =
            new CronquistClassification(
                Objects.requireNonNull(
                    findExistingRank(
                        toInsertClassification.get(offset + i)
                    )
                )
            )
                .getClassificationAscendante();

        int index = i;
        existingClassificationList.get(index - 1).getParent().setId(toBeMergedClassificationList.get(0).getId());
        CronquistRank existant = existingClassificationList.get(index);
        while (isRangsIntermediaires(existant)) {
            /*
             * Supprime la portion de classification obsolète
             * Les IDs seront ajoutés lors des prochaines itérations sur les rangs ascendants
             */
            rangsIntermediairesASupprimer.add(existant);
            existingClassificationList.set(index, toBeMergedClassificationList.get(index - i));
            existant = existingClassificationList.get(++index);
        }

        // Déplace la portion descendante de la section intermédiaire existante
        index = i - 1;
        existant = existingClassificationList.get(index);
        CronquistRank toInsert = toInsertClassification.get(offset + index);
        while (isRangsIntermediaires(existant)) {
            toInsert.getParent().setId(existant.getParent().getId());

            existant = existingClassificationList.get(--index);
            toInsert = toInsertClassification.get(offset + index);
        }

    }

    private void synchronizeUrl(@NotNull List<CronquistRank> existingClassificationList, int offset, int i) {
        CronquistRank existingRank, rankToInsert;
        existingRank = existingClassificationList.get(i);
        rankToInsert = toInsertClassification.get(offset + i);

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

    public Set<CronquistRank> getRangsIntermediairesASupprimer() {
        return rangsIntermediairesASupprimer;
    }

    public List<CronquistRank> getToInsertClassification() {
        return toInsertClassification;
    }
}
