package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Ressemblance;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Ressemblance}.
 */
public interface RessemblanceService {
    /**
     * Save a ressemblance.
     *
     * @param ressemblance the entity to save.
     * @return the persisted entity.
     */
    Ressemblance save(Ressemblance ressemblance);

    /**
     * Partially updates a ressemblance.
     *
     * @param ressemblance the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ressemblance> partialUpdate(Ressemblance ressemblance);

    /**
     * Get all the ressemblances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ressemblance> findAll(Pageable pageable);

    /**
     * Get the "id" ressemblance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ressemblance> findOne(Long id);

    /**
     * Delete the "id" ressemblance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
