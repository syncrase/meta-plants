package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.ExpositionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Exposition}.
 */
public interface ExpositionService {
    /**
     * Save a exposition.
     *
     * @param expositionDTO the entity to save.
     * @return the persisted entity.
     */
    ExpositionDTO save(ExpositionDTO expositionDTO);

    /**
     * Partially updates a exposition.
     *
     * @param expositionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExpositionDTO> partialUpdate(ExpositionDTO expositionDTO);

    /**
     * Get all the expositions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExpositionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" exposition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExpositionDTO> findOne(Long id);

    /**
     * Delete the "id" exposition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
