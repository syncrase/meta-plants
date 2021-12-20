package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.repository.UrlRepository;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.UrlQueryService;
import fr.syncrase.ecosyst.service.criteria.CronquistRankCriteria;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CronquistService {

    /**
     * Deux rangs taxonomiques sont séparés par d'autres rangs dont on ne connait pas forcément le nom.<br>
     * Une valeur par défaut permet de lier ces deux rangs avec des rangs vides.<br>
     * Si ultérieurement ces rangs sont déterminés, les valeurs par défaut sont mises à jour
     */
    public static final String DEFAULT_NAME_FOR_CONNECTOR_RANK = null;
    private final Logger log = LoggerFactory.getLogger(CronquistService.class);

    private CronquistRankRepository superRegneRepository;

    private CronquistRankQueryService superRegneQueryService;

    private UrlRepository urlRepository;

    private UrlQueryService urlQueryService;

    @Autowired
    public void setSuperRegneQueryService(CronquistRankQueryService superRegneQueryService) {
        this.superRegneQueryService = superRegneQueryService;
    }

    @Autowired
    public void setSuperRegneRepository(CronquistRankRepository superRegneRepository) {
        this.superRegneRepository = superRegneRepository;
    }

    @Autowired
    public void setUrlRepository(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Autowired
    public void setUrlQueryService(UrlQueryService urlQueryService) {
        this.urlQueryService = urlQueryService;
    }

    /**
     * Tous les rangs sont mis à jour avec l'ID et les noms, mais ascendant ni descendants <br>
     * Vérifie s'il n'y a pas de différence entre la classification enregistrée et celle que l'on souhaite enregistrer. <br>
     * Par exemple : L'ordre 'Arecales' a une unique ascendance, s'il y a une différence dans l'ascendance, il faut le repérer TODO et la corriger scrapper aussi aussi <br>
     * Vérifie tous les rangs à partir du plus inférieur pour trouver un rang où les taxons correspondent. À partir de celui-là, la classification doit être commune <br>
     *
     * <h1>Pourquoi précharger tous les IDs ?</h1>
     * Ça évite de persister des rangs intermédiaires vides qui seront dé-associer plus bas dans l'arbre quand un rang existant (avec son parent) sera récupéré<br>
     * <b>Chemin unique</b> : Pour chacun des rangs, les sous-rangs ne sont pas récupérés. L'arborescence sans dichotomie est préservée.
     *
     * <h1>Les synonymes</h1>
     * Par définition d'un rang taxonomique, un même rang ne peut avoir qu'une unique arborescence sans dichotomie.<br>
     * Lors de l'insertion d'un rang, si les parents sont différents alors ce sont les mêmes (ie. des synonymes)<br>
     * Ces différents synonymes se positionnent exactement de la même manière dans l'arborescence. Ils ont :<br>
     * <ul>
     *     <li>Les mêmes enfants</li>
     *     <li>Le même parent</li>
     * </ul>
     *
     * @param cronquistClassification side effect
     * @param urlWiki                 url du wiki d'où à été extrait la classification
     */
    public void saveCronquist(@NotNull CronquistClassification cronquistClassification, String urlWiki) {

        // Les rangs inférieurs sans valeur n'ont pas de raison d'être puisque la raison d'être des rangs vides et de lier deux rangs dont les noms sont connus : nettoyage de l'arborescence au commencement
        cronquistClassification.clearTail();
        List<CronquistRank> list = cronquistClassification.getReverseList();
        list.get(0).addUrls(new Url().url(urlWiki));
        // Parcours du plus bas rang jusqu'au plus élevé pour trouver le premier rang existant en base
        CronquistClassification existingClassification = null;
        for (CronquistRank cronquistRank : list) {
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

        setIdsOfExistingRanks(list, existingClassification != null ? existingClassification.getReverseList() : Collections.EMPTY_LIST);
        save(list);
    }

    /**
     * @param cronquistRank Rang dont il faut vérifier la présence en base
     * @return La liste des rangs qui correspondent en base. Soit une liste vide, soit une liste d'un unique élément
     */
    private List<CronquistRank> findExistingRank(@NotNull CronquistRank cronquistRank) {
        if (Objects.equals(cronquistRank.getNomFr(), DEFAULT_NAME_FOR_CONNECTOR_RANK) && cronquistRank.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        CronquistRankCriteria rankCrit = new CronquistRankCriteria();

        StringFilter nomFrFilter = new StringFilter();
        nomFrFilter.setEquals(cronquistRank.getNomFr());
        rankCrit.setNomFr(nomFrFilter);
        CronquistRankCriteria.CronquistTaxonomikRanksFilter rankFilter = new CronquistRankCriteria.CronquistTaxonomikRanksFilter();
        rankFilter.setEquals(cronquistRank.getRank());
        rankCrit.setRank(rankFilter);
        return superRegneQueryService.findByCriteria(rankCrit);
    }


    private void save(@NotNull List<CronquistRank> list) {
        // Etant donné qu'un rang inférieur doit contenir le rang supérieur, l'enregistrement des classifications doit se faire en commençant par le rang supérieur
        int size = list.size() - 1;
        for (int i = size; i >= 0; i--) {
            // TODO question les IDs des parents sont-ils bien contenus dans les enfants ?
            superRegneRepository.save(list.get(i));
            urlRepository.saveAll(list.get(i).getUrls());
        }
    }

    /**
     * Mis à jour des IDs des éléments de la première liste pour qu'ils pointent vers les éléments déjà enregistrés en base
     *
     * @param rankToInsertList           Liste des rangs que l'on souhaite enregistrer. Aucun des rangs n'a d'ID
     * @param existingClassificationList List des rangs de l'arborescence existante en base de données
     */
    private void setIdsOfExistingRanks(@NotNull List<CronquistRank> rankToInsertList, @NotNull List<CronquistRank> existingClassificationList) {
        if (existingClassificationList.size() == 0) {
            log.info("No existing entity in the database");
            return;
        }
        // Les deux premiers items sont égaux
        int offset = rankToInsertList.size() - existingClassificationList.size();
        if (!rankToInsertList.get(offset).getNomFr().equals(existingClassificationList.get(0).getNomFr())) {
            log.error("Erreur : les deux premiers items égaux sont censé être égaux");
            return;
        }

        CronquistRank existingRank, rankToInsert;
        // Parcours des éléments pour ajouter les IDs des éléments connus
        for (int i = 0; i < existingClassificationList.size(); i++) {
            existingRank = existingClassificationList.get(i);
            rankToInsert = rankToInsertList.get(offset + i);
            // Chaine vide && chaine non vide
            if (Objects.equals(rankToInsert.getNomFr(), DEFAULT_NAME_FOR_CONNECTOR_RANK) && !Objects.equals(existingRank.getNomFr(), DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
                // mon rang intermédiaire se trouve avoir un nom en base → ajout du nom et de l'id dans mon objet
                rankToInsert.setId(existingRank.getId());
                rankToInsert.setNomFr(existingRank.getNomFr());
                continue;
            }
            // Quelque current && chaine vide
            if (Objects.equals(existingRank.getNomFr(), DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
                // mon rang avec (ou sans) nom n'en a pas en base → ajout de l'id à mon rang pour que soit update le nom trouvé
                rankToInsert.setId(existingRank.getId());
                continue;
            }
            // Chaine non vide && chaine non vide
            if (!Objects.equals(rankToInsert.getNomFr(), DEFAULT_NAME_FOR_CONNECTOR_RANK) && !Objects.equals(existingRank.getNomFr(), DEFAULT_NAME_FOR_CONNECTOR_RANK)) {
                // Les deux noms existent
                if (!rankToInsert.getNomFr().equals(existingRank.getNomFr())) {
                    // TODO gestion des synonymes (après mise à jour du modèle)
                    // Ils sont différents
                    // En base j'ai un autre nom, je la crois et je met à jour mon objet
                    rankToInsert.setId(existingRank.getId());
                    rankToInsert.setNomFr(existingRank.getNomFr());
                } else {
                    // Ils sont égaux
                    rankToInsert.setId(existingRank.getId());
                }
            }
        }
    }

}
