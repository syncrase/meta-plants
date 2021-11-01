package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.PeriodeAnneeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.PeriodeAnnee}.
 */
public interface PeriodeAnneeService {

    /**
     * Save a periodeAnnee.
     *
     * @param periodeAnneeDTO the entity to save.
     * @return the persisted entity.
     */
    PeriodeAnneeDTO save(PeriodeAnneeDTO periodeAnneeDTO);

    /**
     * Get all the periodeAnnees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PeriodeAnneeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" periodeAnnee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PeriodeAnneeDTO> findOne(Long id);

    /**
     * Delete the "id" periodeAnnee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
