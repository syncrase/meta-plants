package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.criteria.CronquistRankCriteria;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.service.filter.StringFilter;

import java.util.*;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistUtils.isRangIntermediaire;

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

    private final MergeResult persistanceResult;
    private CronquistClassification existingClassification;
    private final CronquistRankQueryService cronquistRankQueryService;


    public CronquistClassificationMerger(@NotNull CronquistClassification cronquistClassification, String urlWiki, CronquistRankQueryService cronquistRankQueryService) {
        this.cronquistRankQueryService = cronquistRankQueryService;
//        List<CronquistRank> list, CronquistClassification existingClassification
//        this.existingClassification = existingClassification;
        persistanceResult = new MergeResult();
        persistanceResult.rangsIntermediairesASupprimer = new ArrayList<>();


        cronquistClassification.clearTail();
        persistanceResult.list  = cronquistClassification.getClassificationAscendante();
        persistanceResult.list.get(0).addUrls(new Url().url(urlWiki));
        // Parcours du plus bas rang jusqu'au plus élevé pour trouver le premier rang existant en base
//        CronquistClassification existingClassification = null;
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
            return Collections.EMPTY_LIST;
        }
        CronquistRankCriteria rankCrit = new CronquistRankCriteria();

        StringFilter nomFrFilter = new StringFilter();
        nomFrFilter.setEquals(cronquistRank.getNomFr());
        rankCrit.setNomFr(nomFrFilter);
        CronquistRankCriteria.CronquistTaxonomikRanksFilter rankFilter = new CronquistRankCriteria.CronquistTaxonomikRanksFilter();
        rankFilter.setEquals(cronquistRank.getRank());
        rankCrit.setRank(rankFilter);
        return cronquistRankQueryService.findByCriteria(rankCrit);
    }

    public MergeResult mergeClassifications() {

        updateFieldsWithExisting(
            persistanceResult.list,
            existingClassification != null ? existingClassification.getClassificationAscendante() : Collections.EMPTY_LIST,
            persistanceResult.rangsIntermediairesASupprimer
        );

        return persistanceResult;
    }

    /**
     * Mis à jour des IDs des éléments de la première liste pour qu'ils pointent vers les éléments déjà enregistrés en base
     *
     * @param rankToInsertList              Liste des rangs que l'on souhaite enregistrer. Aucun des rangs n'a d'ID
     * @param existingClassificationList    Liste des rangs de l'arborescence existante en base de données
     * @param rangsIntermediairesASupprimer Liste des rangs intermédiaires à supprimer après le merge de deux branches de classification
     */
    private void updateFieldsWithExisting(@NotNull List<CronquistRank> rankToInsertList, @NotNull List<CronquistRank> existingClassificationList, List<Long> rangsIntermediairesASupprimer) {
        if (existingClassificationList.size() == 0) {
            log.info("No existing entity in the database");
            return;
        }
        int offset = rankToInsertList.size() - existingClassificationList.size();
        if (!rankToInsertList.get(offset).getNomFr().equals(existingClassificationList.get(0).getNomFr())) {
            log.error("At the depth of the first known rank, ranks names must be equals");
            return;
        }

        // Parcours des éléments pour ajouter les IDs des éléments connus
        for (int i = 0; i < existingClassificationList.size(); i++) {
            updateNameAndId(existingClassificationList, rankToInsertList, offset, i, rangsIntermediairesASupprimer);
            updateUrl(existingClassificationList, rankToInsertList, offset, i);
        }
    }

    private void updateNameAndId(@NotNull List<CronquistRank> existingClassificationList, @NotNull List<CronquistRank> rankToInsertList, int offset, int i, List<Long> rangsIntermediairesASupprimer) {

        CronquistRank existingRank, rankToInsert;
        existingRank = existingClassificationList.get(i);
        rankToInsert = rankToInsertList.get(offset + i);

        if (isRangIntermediaire(rankToInsert) && !isRangIntermediaire(existingRank)) {
            // mon rang intermédiaire se trouve avoir un nom en base → ajout du nom et de l'id dans mon objet
            rankToInsert.setId(existingRank.getId());
            rankToInsert.setNomFr(existingRank.getNomFr());
            return;
        }
        if (isRangIntermediaire(rankToInsert) && isRangIntermediaire(existingRank)) {
            rankToInsert.setId(existingRank.getId());
            return;
        }
        if (!isRangIntermediaire(rankToInsert) && isRangIntermediaire(existingRank)) {
            List<CronquistRank> rankReplacingTheIntermediateRankList = findExistingRank(rankToInsert);
            if (rankReplacingTheIntermediateRankList.size() > 0) {// Must be = 1. Check if > 1 and throw error ?
                CronquistRank rankReplacingTheIntermediateRank = rankReplacingTheIntermediateRankList.get(0);
                rankToInsert.setId(rankReplacingTheIntermediateRank.getId());
//                rankToInsert.getChildren().addAll(rankReplacingTheIntermediateRank.getChildren());
                rankToInsert.getParent().setId(rankReplacingTheIntermediateRank.getParent().getId());
                /*
                 Remplace la portion de classification ascendante
                 depuis le rang intermédiaire existant jusqu'au rang taxonomique suivant
                 par cette même portion au-dessus du rang taxonomique récupéré
                 et stocke-les ids des rangs intermédiaires qui ont été déconnectés pour pouvoir les supprimer
                 */
                CronquistClassification cronquistClassification = new CronquistClassification(rankReplacingTheIntermediateRank);
                List<CronquistRank> classificationReverseList = cronquistClassification.getClassificationAscendante();
                for (int index = i; index < existingClassificationList.size(); index++) {
                    if (existingClassificationList.get(index).equals(classificationReverseList.get(index - i))) {
                        break;
                    }
                    rangsIntermediairesASupprimer.add(existingClassificationList.get(index).getId());
                    existingClassificationList.set(index, classificationReverseList.get(index - i));
                }
            } else {
                // Le rang taxonomique n'existe pas en base → ajoute uniquement l'id du rang intermédiaire qui deviendra alors un rang taxonomique
                rankToInsert.setId(existingRank.getId());
            }
            return;
        }
        if (!isRangIntermediaire(rankToInsert) && !isRangIntermediaire(existingRank)) {
            // Les deux noms existent
            if (!rankToInsert.getNomFr().equals(existingRank.getNomFr())) {
                // Ils sont différents
                // TODO gestion des synonymes (après mise à jour du modèle)
                log.warn("Synonymes: TO BE IMPLEMENTED");
                // En base j'ai un autre nom, je la crois et je mets à jour mon objet
                rankToInsert.setId(existingRank.getId());
                rankToInsert.setNomFr(existingRank.getNomFr());
            } else {
                // Ils sont égaux
                rankToInsert.setId(existingRank.getId());
            }
        }
    }

    private void updateUrl(@NotNull List<CronquistRank> existingClassificationList, @NotNull List<CronquistRank> rankToInsertList, int offset, int i) {

        CronquistRank existingRank, rankToInsert;
        existingRank = existingClassificationList.get(i);
        rankToInsert = rankToInsertList.get(offset + i);

        if (existingRank.getUrls().size() > 0) {
            if (rankToInsert.getUrls().size() > 0) {// Must be = 1. Check > 1 and throw error?
                Url urlsAInserer = new ArrayList<>(rankToInsert.getUrls()).get(0);
                Optional<Url> matchingExistingUrl = existingRank.getUrls().stream()
                    .filter(url -> url.getUrl().equals(urlsAInserer.getUrl()))
                    .findFirst();
                if (matchingExistingUrl.isPresent()) {
                    rankToInsert.getUrls().forEach(url -> url.setId(matchingExistingUrl.get().getId()));
                }
            } else {
                rankToInsert.getUrls().addAll(existingRank.getUrls());
            }
        }
    }

    static class MergeResult {

        List<Long> rangsIntermediairesASupprimer;
        List<CronquistRank> list;
    }
}
