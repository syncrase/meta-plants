package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.CycleDeVie;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CycleDeVie}.
 */
public interface CycleDeVieService {
    /**
     * Save a cycleDeVie.
     *
     * @param cycleDeVie the entity to save.
     * @return the persisted entity.
     */
    CycleDeVie save(CycleDeVie cycleDeVie);

    /**
     * Partially updates a cycleDeVie.
     *
     * @param cycleDeVie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CycleDeVie> partialUpdate(CycleDeVie cycleDeVie);

    /**
     * Get all the cycleDeVies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CycleDeVie> findAll(Pageable pageable);

    /**
     * Get the "id" cycleDeVie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CycleDeVie> findOne(Long id);

    /**
     * Delete the "id" cycleDeVie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
