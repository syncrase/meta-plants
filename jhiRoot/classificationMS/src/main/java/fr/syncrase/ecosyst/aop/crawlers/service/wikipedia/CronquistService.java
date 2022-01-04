package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.repository.UrlRepository;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import fr.syncrase.ecosyst.service.UrlQueryService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CronquistService {

    private final Logger log = LoggerFactory.getLogger(CronquistService.class);

    private CronquistRankRepository cronquistRankRepositoryRepository;

    private CronquistRankQueryService cronquistRankQueryService;

    private UrlRepository urlRepository;

    private UrlQueryService urlQueryService;

    @Autowired
    public void setCronquistRankQueryService(CronquistRankQueryService cronquistRankQueryService) {
        this.cronquistRankQueryService = cronquistRankQueryService;
    }

    @Autowired
    public void setCronquistRankRepositoryRepository(CronquistRankRepository cronquistRankRepositoryRepository) {
        this.cronquistRankRepositoryRepository = cronquistRankRepositoryRepository;
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
     * Par exemple : L'ordre 'Arecales' a une unique ascendance, s'il y a une différence dans l'ascendance, il faut le repérer <br>
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
     * @param urlWiki                 url du wiki d'où a été extrait la classification
     */
    public void saveCronquist(@NotNull CronquistClassification cronquistClassification, String urlWiki) {

        CronquistClassificationMerger.MergeResult aReturn = new CronquistClassificationMerger(cronquistClassification, urlWiki, cronquistRankQueryService).mergeClassifications();

        save(aReturn.list);
        removeObsoleteInermediatesRanks(aReturn.rangsIntermediairesASupprimer);
    }

    private void removeObsoleteInermediatesRanks(List<Long> rangsIntermediairesASupprimer) {
        cronquistRankRepositoryRepository.deleteAllById(rangsIntermediairesASupprimer);
    }


    private void save(@NotNull List<CronquistRank> list) {
        // Etant donné qu'un rang inférieur doit contenir le rang supérieur, l'enregistrement des classifications doit se faire en commençant par le rang supérieur
        int size = list.size() - 1;
        for (int i = size; i >= 0; i--) {
            cronquistRankRepositoryRepository.save(list.get(i));
            urlRepository.saveAll(list.get(i).getUrls());
        }
    }

}
