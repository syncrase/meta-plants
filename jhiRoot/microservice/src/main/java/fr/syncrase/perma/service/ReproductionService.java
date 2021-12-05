package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.ReproductionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Reproduction}.
 */
public interface ReproductionService {
    /**
     * Save a reproduction.
     *
     * @param reproductionDTO the entity to save.
     * @return the persisted entity.
     */
    ReproductionDTO save(ReproductionDTO reproductionDTO);

    /**
     * Partially updates a reproduction.
     *
     * @param reproductionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReproductionDTO> partialUpdate(ReproductionDTO reproductionDTO);

    /**
     * Get all the reproductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReproductionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reproduction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReproductionDTO> findOne(Long id);

    /**
     * Delete the "id" reproduction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
