package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.RessemblanceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Ressemblance}.
 */
public interface RessemblanceService {

    /**
     * Save a ressemblance.
     *
     * @param ressemblanceDTO the entity to save.
     * @return the persisted entity.
     */
    RessemblanceDTO save(RessemblanceDTO ressemblanceDTO);

    /**
     * Get all the ressemblances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RessemblanceDTO> findAll(Pageable pageable);


    /**
     * Get the "id" ressemblance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RessemblanceDTO> findOne(Long id);

    /**
     * Delete the "id" ressemblance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
