package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.StrateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Strate}.
 */
public interface StrateService {
    /**
     * Save a strate.
     *
     * @param strateDTO the entity to save.
     * @return the persisted entity.
     */
    StrateDTO save(StrateDTO strateDTO);

    /**
     * Partially updates a strate.
     *
     * @param strateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StrateDTO> partialUpdate(StrateDTO strateDTO);

    /**
     * Get all the strates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StrateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" strate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StrateDTO> findOne(Long id);

    /**
     * Delete the "id" strate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
