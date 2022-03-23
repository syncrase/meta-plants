package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.*;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.repository.UrlRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Service
public class CronquistService {

    private final Logger log = LoggerFactory.getLogger(CronquistService.class);

    private CronquistRankRepository cronquistRankRepository;
    private ClassificationNomRepository classificationNomRepository;
    private UrlRepository urlRepository;
    private CronquistClassificationSynchronizer synchronizedClassification;
    private ClassificationRepository classificationRepository;


    public CronquistService() {
    }

    @Autowired
    public void setClassificationNomRepository(ClassificationNomRepository classificationNomRepository) {
        this.classificationNomRepository = classificationNomRepository;
    }

    @Autowired
    public void setCronquistRankRepository(CronquistRankRepository cronquistRankRepository) {
        this.cronquistRankRepository = cronquistRankRepository;
    }

    @Autowired
    public void setUrlRepository(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    @Autowired
    public void setClassificationRepository(ClassificationRepository classificationRepository) {
        synchronizedClassification = new CronquistClassificationSynchronizer(classificationRepository);
        this.classificationRepository = classificationRepository;
    }

    /**
     * Tous les rangs sont mis à jour avec l'ID et les noms, mais ascendant ni descendants <br>
     * Vérifie s'il n'y a pas de différence entre la classification enregistrée et celle que l'on souhaite enregistrer. <br>
     * Par exemple : L'ordre 'Arecales' a une unique ascendance, s'il y a une différence dans l'ascendance, il faut le repérer <br>
     * Vérifie tous les rangs à partir du plus inférieur pour trouver un rang où les taxons correspondent. À partir de celui-là, la classification doit être commune <br>
     *
     * <h1>Pourquoi précharger tous les IDs ?</h1>
     * Ça évite de persister des rangs intermédiaires vides qui seront dé-associés plus bas dans l'arbre quand un rang existant (avec son getRangSuperieur) sera récupéré<br>
     * <b>Chemin unique</b> : Pour chacun des rangs, les sous-rangs ne sont pas récupérés. L'arborescence sans dichotomie est préservée.
     *
     * <h1>Les synonymes</h1>
     * Par définition d'un rang taxonomique, un même rang ne peut avoir qu'une unique arborescence sans dichotomie.<br>
     * Lors de l'insertion d'un rang, si les parents sont différents alors ce sont les mêmes (c.-à-d. Des synonymes)<br>
     * Ces différents synonymes se positionnent exactement de la même manière dans l'arborescence. Ils ont :<br>
     * <ul>
     *     <li>Les mêmes enfants</li>
     *     <li>Le même getRangSuperieur</li>
     * </ul>
     *  @param cronquistClassification side effect
     *
     * @param urlWiki url du wiki d'où a été extrait la classification
     * @return
     */
    @Transactional
    public Collection<CronquistRank> saveCronquist(@NotNull CronquistClassificationBranch cronquistClassification, String urlWiki) {

        log.info("Traitement pré-enregistrement de la classification extraite de '" + urlWiki + "'");
        try {
            synchronizedClassification.applyConsistency(cronquistClassification, urlWiki);

            log.info("Enregistrement de la classification");
            if (cronquistClassification.getClassification() != null) {
                CronquistRankMapper mapper = new CronquistRankMapper();
                return save(mapper.getClassificationToSave(cronquistClassification.getClassificationBranch()));// TODO Use mapper atomic to dbObject and vice versa
            }
            //            if (synchronizedClassification.getRangsASupprimer() != null) {
            //                removeObsoleteIntermediatesRanks(synchronizedClassification.getRangsASupprimer());
            //            }
        } catch (ClassificationReconstructionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void removeObsoleteIntermediatesRanks(@NotNull Set<CronquistRank> rangsIntermediairesASupprimer) {
        for (CronquistRank rankToRemove : rangsIntermediairesASupprimer) {
            classificationNomRepository.deleteAll(rankToRemove.getNoms());
            cronquistRankRepository.delete(rankToRemove);
        }
    }

    /**
     * Enregistre la classification du rang supérieur (Super règne) jusqu'au rang le plus profond
     *
     * @param flatClassification classification à enregistrer
     * @return
     */
    private @NotNull Collection<CronquistRank> save(@NotNull Collection<CronquistRank> flatClassification) {
        // L'enregistrement des classifications doit se faire en commençant par le rang supérieur
        for (CronquistRank rank : flatClassification) {
            cronquistRankRepository.save(rank);
            classificationNomRepository.saveAll(rank.getNoms());
            urlRepository.saveAll(rank.getUrls());
        }
        return flatClassification;
    }

    public CronquistClassificationBranch getClassificationBranchOfThisRank(Long id) {
        return classificationRepository.fetchExistingClassification(new AtomicCronquistRank().id(id));
    }

    public Set<AtomicCronquistRank> getTaxons(Long id) {
        classificationRepository.getTaxons(new AtomicCronquistRank().id(id));
        return null;
    }
}
