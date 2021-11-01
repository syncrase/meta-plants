package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.RaunkierDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Raunkier}.
 */
public interface RaunkierService {

    /**
     * Save a raunkier.
     *
     * @param raunkierDTO the entity to save.
     * @return the persisted entity.
     */
    RaunkierDTO save(RaunkierDTO raunkierDTO);

    /**
     * Get all the raunkiers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RaunkierDTO> findAll(Pageable pageable);


    /**
     * Get the "id" raunkier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RaunkierDTO> findOne(Long id);

    /**
     * Delete the "id" raunkier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
