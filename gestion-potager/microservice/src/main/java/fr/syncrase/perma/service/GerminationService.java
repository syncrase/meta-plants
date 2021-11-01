package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.GerminationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Germination}.
 */
public interface GerminationService {

    /**
     * Save a germination.
     *
     * @param germinationDTO the entity to save.
     * @return the persisted entity.
     */
    GerminationDTO save(GerminationDTO germinationDTO);

    /**
     * Get all the germinations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GerminationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" germination.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GerminationDTO> findOne(Long id);

    /**
     * Delete the "id" germination.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
