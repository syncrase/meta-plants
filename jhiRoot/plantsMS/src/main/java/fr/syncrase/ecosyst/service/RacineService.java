package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Racine;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Racine}.
 */
public interface RacineService {
    /**
     * Save a racine.
     *
     * @param racine the entity to save.
     * @return the persisted entity.
     */
    Racine save(Racine racine);

    /**
     * Partially updates a racine.
     *
     * @param racine the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Racine> partialUpdate(Racine racine);

    /**
     * Get all the racines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Racine> findAll(Pageable pageable);

    /**
     * Get the "id" racine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Racine> findOne(Long id);

    /**
     * Delete the "id" racine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
