package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.APGIVDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.APGIV}.
 */
public interface APGIVService {

    /**
     * Save a aPGIV.
     *
     * @param aPGIVDTO the entity to save.
     * @return the persisted entity.
     */
    APGIVDTO save(APGIVDTO aPGIVDTO);

    /**
     * Get all the aPGIVS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<APGIVDTO> findAll(Pageable pageable);


    /**
     * Get the "id" aPGIV.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<APGIVDTO> findOne(Long id);

    /**
     * Delete the "id" aPGIV.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
