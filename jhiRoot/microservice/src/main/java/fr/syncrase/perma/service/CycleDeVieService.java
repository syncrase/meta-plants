package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.CycleDeVieDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.CycleDeVie}.
 */
public interface CycleDeVieService {
    /**
     * Save a cycleDeVie.
     *
     * @param cycleDeVieDTO the entity to save.
     * @return the persisted entity.
     */
    CycleDeVieDTO save(CycleDeVieDTO cycleDeVieDTO);

    /**
     * Partially updates a cycleDeVie.
     *
     * @param cycleDeVieDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CycleDeVieDTO> partialUpdate(CycleDeVieDTO cycleDeVieDTO);

    /**
     * Get all the cycleDeVies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CycleDeVieDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cycleDeVie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CycleDeVieDTO> findOne(Long id);

    /**
     * Delete the "id" cycleDeVie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
