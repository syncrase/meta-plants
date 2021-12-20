package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Allelopathie;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Allelopathie}.
 */
public interface AllelopathieService {
    /**
     * Save a allelopathie.
     *
     * @param allelopathie the entity to save.
     * @return the persisted entity.
     */
    Allelopathie save(Allelopathie allelopathie);

    /**
     * Partially updates a allelopathie.
     *
     * @param allelopathie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Allelopathie> partialUpdate(Allelopathie allelopathie);

    /**
     * Get all the allelopathies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Allelopathie> findAll(Pageable pageable);

    /**
     * Get the "id" allelopathie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Allelopathie> findOne(Long id);

    /**
     * Delete the "id" allelopathie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
