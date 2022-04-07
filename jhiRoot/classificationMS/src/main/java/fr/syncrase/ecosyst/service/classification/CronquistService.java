package fr.syncrase.ecosyst.service.classification;

import fr.syncrase.ecosyst.domain.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.classification.consistency.CronquistClassificationConsistency;
import fr.syncrase.ecosyst.domain.classification.entities.atomic.AtomicClassificationNom;
import fr.syncrase.ecosyst.domain.classification.entities.atomic.AtomicCronquistRank;
import fr.syncrase.ecosyst.domain.classification.entities.atomic.AtomicUrl;
import fr.syncrase.ecosyst.domain.classification.consistency.ClassificationReconstructionException;
import fr.syncrase.ecosyst.domain.classification.consistency.InconsistentRank;
import fr.syncrase.ecosyst.repository.MoreThanOneResultException;
import fr.syncrase.ecosyst.repository.UnknownRankId;
import fr.syncrase.ecosyst.repository.ClassificationReader;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import fr.syncrase.ecosyst.domain.classification.entities.ICronquistRank;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class CronquistService implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(CronquistService.class);
    private CronquistClassificationConsistency cronquistClassificationConsistency;
    private ClassificationRepository classificationRepository;

    private ClassificationReader classificationReader;


    public CronquistService() {
    }

    @Autowired
    public void setClassificationRepository(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    @Autowired
    public void setCronquistRankReader(ClassificationReader classificationReader) {
        this.classificationReader = classificationReader;
    }

    @Override
    public void afterPropertiesSet() {
        cronquistClassificationConsistency = new CronquistClassificationConsistency(classificationRepository, classificationReader);
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
     * @return La branche de classification enregistrée
     */
    @Transactional
    public CronquistClassificationBranch saveCronquist(@NotNull CronquistClassificationBranch scrapedClassification, String urlWiki) {

        log.info("Traitement pré-enregistrement de la classification extraite de '" + urlWiki + "'");
        try {
            scrapedClassification.getRangDeBase().addUrl(AtomicUrl.newAtomicUrl(urlWiki));
            CronquistClassificationBranch consistentClassification = cronquistClassificationConsistency.getConsistentClassification(scrapedClassification);

            log.info("Enregistrement de la classification");
            return classificationRepository.save(consistentClassification);
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

    @Transactional
    public CronquistClassificationBranch getClassificationById(Long id) {
        try {
            return classificationReader.findExistingClassification(new AtomicCronquistRank().id(id));
        } catch (ClassificationReconstructionException e) {
            log.error("Impossible de reconstruire la classification à partir du rang obtenu");
        } catch (MoreThanOneResultException e) {
            log.error("Impossible de récupérer un unique rang à partir des informations procurées");
        }
        return null;
    }

    @Transactional
    public CronquistClassificationBranch getClassificationByName(String chironia) {
        try {
            return classificationReader.findExistingClassification(new AtomicCronquistRank().addNom(new AtomicClassificationNom().nomFr(chironia)));
        } catch (ClassificationReconstructionException e) {
            log.error("Impossible de reconstruire la classification à partir du rang obtenu");
        } catch (MoreThanOneResultException e) {
            log.error("Impossible de récupérer un unique rang à partir des informations procurées");
        }
        return null;
    }

    @Transactional
    public Set<ICronquistRank> getTaxonsOf(Long id) {
        return classificationReader.getTaxons(new AtomicCronquistRank().id(id));
    }

    public ICronquistRank getRankById(Long id) {
        try {
            return classificationReader.findExistingRank(new AtomicCronquistRank().id(id));
        } catch (MoreThanOneResultException e) {
            log.error("Impossible de récupérer un unique rang à partir des informations procurées");
        }
        return null;
    }
}
