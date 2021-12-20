package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Strate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Strate}.
 */
public interface StrateService {
    /**
     * Save a strate.
     *
     * @param strate the entity to save.
     * @return the persisted entity.
     */
    Strate save(Strate strate);

    /**
     * Partially updates a strate.
     *
     * @param strate the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Strate> partialUpdate(Strate strate);

    /**
     * Get all the strates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Strate> findAll(Pageable pageable);

    /**
     * Get the "id" strate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Strate> findOne(Long id);

    /**
     * Delete the "id" strate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
