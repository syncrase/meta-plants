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
     * Tous les rangs sont mis ?? jour avec l'ID et les noms, mais ascendant ni descendants <br>
     * V??rifie s'il n'y a pas de diff??rence entre la classification enregistr??e et celle que l'on souhaite enregistrer. <br>
     * Par exemple : L'ordre 'Arecales' a une unique ascendance, s'il y a une diff??rence dans l'ascendance, il faut le rep??rer <br>
     * V??rifie tous les rangs ?? partir du plus inf??rieur pour trouver un rang o?? les taxons correspondent. ?? partir de celui-l??, la classification doit ??tre commune <br>
     *
     * <h1>Pourquoi pr??charger tous les IDs ?</h1>
     * ??a ??vite de persister des rangs interm??diaires vides qui seront d??-associ??s plus bas dans l'arbre quand un rang existant (avec son getRangSuperieur) sera r??cup??r??<br>
     * <b>Chemin unique</b> : Pour chacun des rangs, les sous-rangs ne sont pas r??cup??r??s. L'arborescence sans dichotomie est pr??serv??e.
     *
     * <h1>Les synonymes</h1>
     * Par d??finition d'un rang taxonomique, un m??me rang ne peut avoir qu'une unique arborescence sans dichotomie.<br>
     * Lors de l'insertion d'un rang, si les parents sont diff??rents alors ce sont les m??mes (c.-??-d. Des synonymes)<br>
     * Ces diff??rents synonymes se positionnent exactement de la m??me mani??re dans l'arborescence. Ils ont :<br>
     * <ul>
     *     <li>Les m??mes enfants</li>
     *     <li>Le m??me getRangSuperieur</li>
     * </ul>
     *  @param scrapedClassification side effect
     *
     * @param urlWiki url du wiki d'o?? a ??t?? extrait la classification
     * @return La branche de classification enregistr??e
     */
    @Transactional
    public CronquistClassificationBranch saveCronquist(@NotNull CronquistClassificationBranch scrapedClassification, String urlWiki) {

        log.info("Traitement pr??-enregistrement de la classification extraite de '" + urlWiki + "'");
        try {
            scrapedClassification.getRangDeBase().addUrl(AtomicUrl.newAtomicUrl(urlWiki));
            CronquistClassificationBranch consistentClassification = cronquistClassificationConsistency.getConsistentClassification(scrapedClassification);

            log.info("Enregistrement de la classification");
            return classificationRepository.save(consistentClassification);
        } catch (ClassificationReconstructionException e) {
            log.error("Impossible de reconstruire la classification. " + e.getMessage());
        } catch (UnknownRankId e) {
            log.error("Impossible de r??cup??rer le rang. " + e.getMessage());
        } catch (MoreThanOneResultException e) {
            log.error("Impossible de r??cup??rer un unique rang. " + e.getMessage());
        } catch (InconsistentRank e) {
            log.error("Impossible d'enregistrer le rang car une incoh??rence ?? ??t?? d??tect??e. " + e.getMessage());
        }
        return null;
    }

    @Transactional
    public CronquistClassificationBranch getClassificationById(Long id) {
        try {
            return classificationReader.findExistingClassification(new AtomicCronquistRank().id(id));
        } catch (ClassificationReconstructionException e) {
            log.error("Impossible de reconstruire la classification ?? partir du rang obtenu");
        } catch (MoreThanOneResultException e) {
            log.error("Impossible de r??cup??rer un unique rang ?? partir des informations procur??es");
        }
        return null;
    }

    @Transactional
    public CronquistClassificationBranch getClassificationByName(String chironia) {
        try {
            return classificationReader.findExistingClassification(new AtomicCronquistRank().addNom(new AtomicClassificationNom().nomFr(chironia)));
        } catch (ClassificationReconstructionException e) {
            log.error("Impossible de reconstruire la classification ?? partir du rang obtenu");
        } catch (MoreThanOneResultException e) {
            log.error("Impossible de r??cup??rer un unique rang ?? partir des informations procur??es");
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
            log.error("Impossible de r??cup??rer un unique rang ?? partir des informations procur??es");
        }
        return null;
    }
}
