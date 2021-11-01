package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.AllelopathieDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Allelopathie}.
 */
public interface AllelopathieService {

    /**
     * Save a allelopathie.
     *
     * @param allelopathieDTO the entity to save.
     * @return the persisted entity.
     */
    AllelopathieDTO save(AllelopathieDTO allelopathieDTO);

    /**
     * Get all the allelopathies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AllelopathieDTO> findAll(Pageable pageable);


    /**
     * Get the "id" allelopathie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AllelopathieDTO> findOne(Long id);

    /**
     * Delete the "id" allelopathie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
