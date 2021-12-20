package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Reproduction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Reproduction}.
 */
public interface ReproductionService {
    /**
     * Save a reproduction.
     *
     * @param reproduction the entity to save.
     * @return the persisted entity.
     */
    Reproduction save(Reproduction reproduction);

    /**
     * Partially updates a reproduction.
     *
     * @param reproduction the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Reproduction> partialUpdate(Reproduction reproduction);

    /**
     * Get all the reproductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Reproduction> findAll(Pageable pageable);

    /**
     * Get the "id" reproduction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Reproduction> findOne(Long id);

    /**
     * Delete the "id" reproduction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
