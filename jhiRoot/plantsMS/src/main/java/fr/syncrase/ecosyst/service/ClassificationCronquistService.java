package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.ClassificationCronquist;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ClassificationCronquist}.
 */
public interface ClassificationCronquistService {
    /**
     * Save a classificationCronquist.
     *
     * @param classificationCronquist the entity to save.
     * @return the persisted entity.
     */
    ClassificationCronquist save(ClassificationCronquist classificationCronquist);

    /**
     * Partially updates a classificationCronquist.
     *
     * @param classificationCronquist the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClassificationCronquist> partialUpdate(ClassificationCronquist classificationCronquist);

    /**
     * Get all the classificationCronquists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassificationCronquist> findAll(Pageable pageable);

    /**
     * Get the "id" classificationCronquist.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassificationCronquist> findOne(Long id);

    /**
     * Delete the "id" classificationCronquist.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
