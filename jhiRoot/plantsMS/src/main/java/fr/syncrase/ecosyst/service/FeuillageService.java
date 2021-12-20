package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Feuillage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Feuillage}.
 */
public interface FeuillageService {
    /**
     * Save a feuillage.
     *
     * @param feuillage the entity to save.
     * @return the persisted entity.
     */
    Feuillage save(Feuillage feuillage);

    /**
     * Partially updates a feuillage.
     *
     * @param feuillage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Feuillage> partialUpdate(Feuillage feuillage);

    /**
     * Get all the feuillages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Feuillage> findAll(Pageable pageable);

    /**
     * Get the "id" feuillage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Feuillage> findOne(Long id);

    /**
     * Delete the "id" feuillage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
