package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.SemisDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Semis}.
 */
public interface SemisService {
    /**
     * Save a semis.
     *
     * @param semisDTO the entity to save.
     * @return the persisted entity.
     */
    SemisDTO save(SemisDTO semisDTO);

    /**
     * Partially updates a semis.
     *
     * @param semisDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SemisDTO> partialUpdate(SemisDTO semisDTO);

    /**
     * Get all the semis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SemisDTO> findAll(Pageable pageable);

    /**
     * Get the "id" semis.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SemisDTO> findOne(Long id);

    /**
     * Delete the "id" semis.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
