package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Sol;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Sol}.
 */
public interface SolService {
    /**
     * Save a sol.
     *
     * @param sol the entity to save.
     * @return the persisted entity.
     */
    Sol save(Sol sol);

    /**
     * Partially updates a sol.
     *
     * @param sol the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sol> partialUpdate(Sol sol);

    /**
     * Get all the sols.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sol> findAll(Pageable pageable);

    /**
     * Get the "id" sol.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sol> findOne(Long id);

    /**
     * Delete the "id" sol.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
