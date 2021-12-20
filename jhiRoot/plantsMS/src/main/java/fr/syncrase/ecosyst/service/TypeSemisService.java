package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.TypeSemis;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TypeSemis}.
 */
public interface TypeSemisService {
    /**
     * Save a typeSemis.
     *
     * @param typeSemis the entity to save.
     * @return the persisted entity.
     */
    TypeSemis save(TypeSemis typeSemis);

    /**
     * Partially updates a typeSemis.
     *
     * @param typeSemis the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeSemis> partialUpdate(TypeSemis typeSemis);

    /**
     * Get all the typeSemis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeSemis> findAll(Pageable pageable);

    /**
     * Get the "id" typeSemis.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeSemis> findOne(Long id);

    /**
     * Delete the "id" typeSemis.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
