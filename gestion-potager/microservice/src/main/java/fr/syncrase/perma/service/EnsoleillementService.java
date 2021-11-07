package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.EnsoleillementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Ensoleillement}.
 */
public interface EnsoleillementService {
    /**
     * Save a ensoleillement.
     *
     * @param ensoleillementDTO the entity to save.
     * @return the persisted entity.
     */
    EnsoleillementDTO save(EnsoleillementDTO ensoleillementDTO);

    /**
     * Partially updates a ensoleillement.
     *
     * @param ensoleillementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnsoleillementDTO> partialUpdate(EnsoleillementDTO ensoleillementDTO);

    /**
     * Get all the ensoleillements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnsoleillementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ensoleillement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnsoleillementDTO> findOne(Long id);

    /**
     * Delete the "id" ensoleillement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
