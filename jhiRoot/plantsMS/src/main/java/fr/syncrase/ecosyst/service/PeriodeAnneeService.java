package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PeriodeAnnee}.
 */
public interface PeriodeAnneeService {
    /**
     * Save a periodeAnnee.
     *
     * @param periodeAnnee the entity to save.
     * @return the persisted entity.
     */
    PeriodeAnnee save(PeriodeAnnee periodeAnnee);

    /**
     * Partially updates a periodeAnnee.
     *
     * @param periodeAnnee the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PeriodeAnnee> partialUpdate(PeriodeAnnee periodeAnnee);

    /**
     * Get all the periodeAnnees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PeriodeAnnee> findAll(Pageable pageable);

    /**
     * Get the "id" periodeAnnee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PeriodeAnnee> findOne(Long id);

    /**
     * Delete the "id" periodeAnnee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
