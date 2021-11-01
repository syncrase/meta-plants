package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.CronquistDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Cronquist}.
 */
public interface CronquistService {

    /**
     * Save a cronquist.
     *
     * @param cronquistDTO the entity to save.
     * @return the persisted entity.
     */
    CronquistDTO save(CronquistDTO cronquistDTO);

    /**
     * Get all the cronquists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CronquistDTO> findAll(Pageable pageable);


    /**
     * Get the "id" cronquist.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CronquistDTO> findOne(Long id);

    /**
     * Delete the "id" cronquist.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
