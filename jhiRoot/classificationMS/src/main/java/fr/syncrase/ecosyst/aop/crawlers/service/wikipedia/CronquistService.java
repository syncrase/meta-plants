package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.ClassificationRepository;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationConsistency;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistRankMapper;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.ClassificationReconstructionException;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.InconsistentRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.MoreThanOneResultException;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions.UnknownRankId;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.ICronquistRank;
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
import java.util.stream.Collectors;

@Service
public class CronquistService {

    private final Logger log = LoggerFactory.getLogger(CronquistService.class);

    private CronquistRankRepository cronquistRankRepository;
    private ClassificationNomRepository classificationNomRepository;
    private UrlRepository urlRepository;
    private CronquistClassificationConsistency synchronizedClassification;
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
        synchronizedClassification = new CronquistClassificationConsistency(classificationRepository);
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
     *  @param scrapedClassification side effect
     *
     * @param urlWiki url du wiki d'où a été extrait la classification
     * @return
     */
    @Transactional
    public Collection<ICronquistRank> saveCronquist(@NotNull CronquistClassificationBranch scrapedClassification, String urlWiki) {

        log.info("Traitement pré-enregistrement de la classification extraite de '" + urlWiki + "'");
        try {
            synchronizedClassification.applyConsistency(scrapedClassification, urlWiki);

            log.info("Enregistrement de la classification");
            if (scrapedClassification.getClassification() != null) {
                CronquistRankMapper mapper = new CronquistRankMapper();
                return save(mapper.getClassificationToSave(scrapedClassification.getClassificationBranch()));// TODO Use mapper atomic to dbObject and vice versa
            }
        } catch (ClassificationReconstructionException e) {
            log.error("Impossible de reconstruire la classification. " + e.getMessage());
        } catch (UnknownRankId e) {
            log.error("Impossible de récupérer le rang. " + e.getMessage());
        } catch (MoreThanOneResultException e) {
            log.error("Impossible de récupérer un unique rang. " + e.getMessage());
        } catch (InconsistentRank e) {
            log.error("Impossible d'enregistrer le rang car une incohérence à été détectée. " + e.getMessage());
        }
        return null;
    }

    /**
     * Enregistre la classification du rang supérieur (Super règne) jusqu'au rang le plus profond
     *
     * @param flatClassification classification à enregistrer
     * @return
     */
    private @NotNull Collection<ICronquistRank> save(@NotNull Collection<CronquistRank> flatClassification) {
        // L'enregistrement des classifications doit se faire en commençant par le rang supérieur
        for (CronquistRank rank : flatClassification) {
            cronquistRankRepository.save(rank);
            classificationNomRepository.saveAll(rank.getNoms());
            urlRepository.saveAll(rank.getUrls());
        }
        return flatClassification.stream().map(AtomicCronquistRank::new).collect(Collectors.toSet());
    }

    @Transactional
    public CronquistClassificationBranch getClassificationById(Long id) {
        return classificationRepository.fetchExistingClassification(new AtomicCronquistRank().id(id));
    }

    @Transactional
    public Set<ICronquistRank> getTaxonsOf(Long id) {
        return classificationRepository.getTaxons(new AtomicCronquistRank().id(id));
    }

    public CronquistClassificationBranch getClassificationByName(String chironia) {
        return classificationRepository.fetchExistingClassification(new AtomicCronquistRank().addNom(new AtomicClassificationNom().nomFr(chironia)));
    }
}
