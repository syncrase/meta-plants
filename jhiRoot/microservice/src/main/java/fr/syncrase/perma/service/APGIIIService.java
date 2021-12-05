package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.APGIIIDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.APGIII}.
 */
public interface APGIIIService {
    /**
     * Save a aPGIII.
     *
     * @param aPGIIIDTO the entity to save.
     * @return the persisted entity.
     */
    APGIIIDTO save(APGIIIDTO aPGIIIDTO);

    /**
     * Partially updates a aPGIII.
     *
     * @param aPGIIIDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<APGIIIDTO> partialUpdate(APGIIIDTO aPGIIIDTO);

    /**
     * Get all the aPGIIIS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<APGIIIDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aPGIII.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<APGIIIDTO> findOne(Long id);

    /**
     * Delete the "id" aPGIII.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
