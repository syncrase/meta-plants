package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.ClassificationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Classification}.
 */
public interface ClassificationService {

    /**
     * Save a classification.
     *
     * @param classificationDTO the entity to save.
     * @return the persisted entity.
     */
    ClassificationDTO save(ClassificationDTO classificationDTO);

    /**
     * Get all the classifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassificationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" classification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassificationDTO> findOne(Long id);

    /**
     * Delete the "id" classification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
