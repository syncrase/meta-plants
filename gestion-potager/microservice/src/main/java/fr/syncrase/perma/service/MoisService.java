package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.MoisDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Mois}.
 */
public interface MoisService {

    /**
     * Save a mois.
     *
     * @param moisDTO the entity to save.
     * @return the persisted entity.
     */
    MoisDTO save(MoisDTO moisDTO);

    /**
     * Get all the mois.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoisDTO> findAll(Pageable pageable);


    /**
     * Get the "id" mois.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoisDTO> findOne(Long id);

    /**
     * Delete the "id" mois.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
