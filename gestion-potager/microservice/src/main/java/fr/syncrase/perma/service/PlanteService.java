package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.PlanteDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
     * Get all the plantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanteDTO> findAll(Pageable pageable);
    /**
     * Get all the PlanteDTO where AllelopathieRecue is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PlanteDTO> findAllWhereAllelopathieRecueIsNull();
    /**
     * Get all the PlanteDTO where AllelopathieProduite is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PlanteDTO> findAllWhereAllelopathieProduiteIsNull();

    /**
     * Get all the plantes with eager load of many-to-many relationships.
     *
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
