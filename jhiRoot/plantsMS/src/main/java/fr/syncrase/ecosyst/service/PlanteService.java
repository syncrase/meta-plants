package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Plante;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Plante}.
 */
public interface PlanteService {
    /**
     * Save a plante.
     *
     * @param plante the entity to save.
     * @return the persisted entity.
     */
    Plante save(Plante plante);

    /**
     * Partially updates a plante.
     *
     * @param plante the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Plante> partialUpdate(Plante plante);

    /**
     * Get all the plantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Plante> findAll(Pageable pageable);

    /**
     * Get all the plantes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Plante> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" plante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Plante> findOne(Long id);

    /**
     * Delete the "id" plante.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
