package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.CycleDeVieDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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
