package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.APGIDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.APGI}.
 */
public interface APGIService {
    /**
     * Save a aPGI.
     *
     * @param aPGIDTO the entity to save.
     * @return the persisted entity.
     */
    APGIDTO save(APGIDTO aPGIDTO);

    /**
     * Partially updates a aPGI.
     *
     * @param aPGIDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<APGIDTO> partialUpdate(APGIDTO aPGIDTO);

    /**
     * Get all the aPGIS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<APGIDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aPGI.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<APGIDTO> findOne(Long id);

    /**
     * Delete the "id" aPGI.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
