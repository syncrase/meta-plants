package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.MoisDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Mois}.
 */
public interface MoisService {
    /**
     * Save a mois.
     *
     * @param moisDTO the entity to save.
     * @return the persisted entity.
     */
    MoisDTO save(MoisDTO moisDTO);

    /**
     * Partially updates a mois.
     *
     * @param moisDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoisDTO> partialUpdate(MoisDTO moisDTO);

    /**
     * Get all the mois.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoisDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mois.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoisDTO> findOne(Long id);

    /**
     * Delete the "id" mois.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
