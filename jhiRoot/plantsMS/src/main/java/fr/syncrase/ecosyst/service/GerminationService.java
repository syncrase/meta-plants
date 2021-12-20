package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Germination;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Germination}.
 */
public interface GerminationService {
    /**
     * Save a germination.
     *
     * @param germination the entity to save.
     * @return the persisted entity.
     */
    Germination save(Germination germination);

    /**
     * Partially updates a germination.
     *
     * @param germination the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Germination> partialUpdate(Germination germination);

    /**
     * Get all the germinations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Germination> findAll(Pageable pageable);

    /**
     * Get the "id" germination.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Germination> findOne(Long id);

    /**
     * Delete the "id" germination.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
