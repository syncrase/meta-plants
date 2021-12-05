package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.FeuillageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Feuillage}.
 */
public interface FeuillageService {
    /**
     * Save a feuillage.
     *
     * @param feuillageDTO the entity to save.
     * @return the persisted entity.
     */
    FeuillageDTO save(FeuillageDTO feuillageDTO);

    /**
     * Partially updates a feuillage.
     *
     * @param feuillageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FeuillageDTO> partialUpdate(FeuillageDTO feuillageDTO);

    /**
     * Get all the feuillages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FeuillageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" feuillage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FeuillageDTO> findOne(Long id);

    /**
     * Delete the "id" feuillage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
