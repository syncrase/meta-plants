package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.SolDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Sol}.
 */
public interface SolService {
    /**
     * Save a sol.
     *
     * @param solDTO the entity to save.
     * @return the persisted entity.
     */
    SolDTO save(SolDTO solDTO);

    /**
     * Partially updates a sol.
     *
     * @param solDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SolDTO> partialUpdate(SolDTO solDTO);

    /**
     * Get all the sols.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SolDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sol.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SolDTO> findOne(Long id);

    /**
     * Delete the "id" sol.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
