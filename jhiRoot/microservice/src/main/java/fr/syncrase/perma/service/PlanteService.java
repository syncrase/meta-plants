package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.PlanteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Plante}.
 */
public interface PlanteService {
    /**
     * Save a plante.
     *
     * @param planteDTO the entity to save.
     * @return the persisted entity.
     */
    PlanteDTO save(PlanteDTO planteDTO);

    /**
     * Partially updates a plante.
     *
     * @param planteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlanteDTO> partialUpdate(PlanteDTO planteDTO);

    /**
     * Get all the plantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanteDTO> findAll(Pageable pageable);

    /**
     * Get all the plantes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" plante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanteDTO> findOne(Long id);

    /**
     * Delete the "id" plante.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
