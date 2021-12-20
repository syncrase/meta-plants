package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Ensoleillement;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Ensoleillement}.
 */
public interface EnsoleillementService {
    /**
     * Save a ensoleillement.
     *
     * @param ensoleillement the entity to save.
     * @return the persisted entity.
     */
    Ensoleillement save(Ensoleillement ensoleillement);

    /**
     * Partially updates a ensoleillement.
     *
     * @param ensoleillement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ensoleillement> partialUpdate(Ensoleillement ensoleillement);

    /**
     * Get all the ensoleillements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ensoleillement> findAll(Pageable pageable);

    /**
     * Get the "id" ensoleillement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ensoleillement> findOne(Long id);

    /**
     * Delete the "id" ensoleillement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
