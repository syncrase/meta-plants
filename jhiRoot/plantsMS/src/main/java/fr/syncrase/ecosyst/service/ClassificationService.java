package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Classification}.
 */
@Service
@Transactional
public class ClassificationService {

    private final Logger log = LoggerFactory.getLogger(ClassificationService.class);

    private final ClassificationRepository classificationRepository;

    public ClassificationService(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    /**
     * Save a classification.
     *
     * @param classification the entity to save.
     * @return the persisted entity.
     */
    public Classification save(Classification classification) {
        log.debug("Request to save Classification : {}", classification);
        return classificationRepository.save(classification);
    }

    /**
     * Partially update a classification.
     *
     * @param classification the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Classification> partialUpdate(Classification classification) {
        log.debug("Request to partially update Classification : {}", classification);

        return classificationRepository
            .findById(classification.getId())
            .map(existingClassification -> {
                if (classification.getNomLatin() != null) {
                    existingClassification.setNomLatin(classification.getNomLatin());
                }

                return existingClassification;
            })
            .map(classificationRepository::save);
    }

    /**
     * Get all the classifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Classification> findAll(Pageable pageable) {
        log.debug("Request to get all Classifications");
        return classificationRepository.findAll(pageable);
    }

    /**
     * Get one classification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Classification> findOne(Long id) {
        log.debug("Request to get Classification : {}", id);
        return classificationRepository.findById(id);
    }

    /**
     * Delete the classification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Classification : {}", id);
        classificationRepository.deleteById(id);
    }
}
