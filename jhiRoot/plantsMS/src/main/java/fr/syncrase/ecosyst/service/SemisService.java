package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Semis;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Semis}.
 */
public interface SemisService {
    /**
     * Save a semis.
     *
     * @param semis the entity to save.
     * @return the persisted entity.
     */
    Semis save(Semis semis);

    /**
     * Partially updates a semis.
     *
     * @param semis the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Semis> partialUpdate(Semis semis);

    /**
     * Get all the semis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Semis> findAll(Pageable pageable);

    /**
     * Get the "id" semis.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Semis> findOne(Long id);

    /**
     * Delete the "id" semis.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
