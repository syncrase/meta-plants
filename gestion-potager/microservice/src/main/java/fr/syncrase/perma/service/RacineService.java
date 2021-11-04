package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.RacineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Racine}.
 */
public interface RacineService {
    /**
     * Save a racine.
     *
     * @param racineDTO the entity to save.
     * @return the persisted entity.
     */
    RacineDTO save(RacineDTO racineDTO);

    /**
     * Partially updates a racine.
     *
     * @param racineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RacineDTO> partialUpdate(RacineDTO racineDTO);

    /**
     * Get all the racines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RacineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" racine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RacineDTO> findOne(Long id);

    /**
     * Delete the "id" racine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
