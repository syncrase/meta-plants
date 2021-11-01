package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.APGIIDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.APGII}.
 */
public interface APGIIService {

    /**
     * Save a aPGII.
     *
     * @param aPGIIDTO the entity to save.
     * @return the persisted entity.
     */
    APGIIDTO save(APGIIDTO aPGIIDTO);

    /**
     * Get all the aPGIIS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<APGIIDTO> findAll(Pageable pageable);


    /**
     * Get the "id" aPGII.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<APGIIDTO> findOne(Long id);

    /**
     * Delete the "id" aPGII.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
