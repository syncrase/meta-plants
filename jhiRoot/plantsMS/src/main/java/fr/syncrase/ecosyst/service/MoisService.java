package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Mois;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Mois}.
 */
public interface MoisService {
    /**
     * Save a mois.
     *
     * @param mois the entity to save.
     * @return the persisted entity.
     */
    Mois save(Mois mois);

    /**
     * Partially updates a mois.
     *
     * @param mois the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Mois> partialUpdate(Mois mois);

    /**
     * Get all the mois.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Mois> findAll(Pageable pageable);

    /**
     * Get the "id" mois.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Mois> findOne(Long id);

    /**
     * Delete the "id" mois.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
