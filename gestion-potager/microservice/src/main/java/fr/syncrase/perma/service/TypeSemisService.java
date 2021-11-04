package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.TypeSemisDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.TypeSemis}.
 */
public interface TypeSemisService {
    /**
     * Save a typeSemis.
     *
     * @param typeSemisDTO the entity to save.
     * @return the persisted entity.
     */
    TypeSemisDTO save(TypeSemisDTO typeSemisDTO);

    /**
     * Partially updates a typeSemis.
     *
     * @param typeSemisDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeSemisDTO> partialUpdate(TypeSemisDTO typeSemisDTO);

    /**
     * Get all the typeSemis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeSemisDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeSemis.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeSemisDTO> findOne(Long id);

    /**
     * Delete the "id" typeSemis.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
