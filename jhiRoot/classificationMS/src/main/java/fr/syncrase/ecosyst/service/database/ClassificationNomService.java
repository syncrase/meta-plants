package fr.syncrase.ecosyst.service.database;

import fr.syncrase.ecosyst.domain.classification.entities.database.ClassificationNom;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ClassificationNom}.
 */
public interface ClassificationNomService {
    /**
     * Save a classificationNom.
     *
     * @param classificationNom the entity to save.
     * @return the persisted entity.
     */
    ClassificationNom save(ClassificationNom classificationNom);

    /**
     * Partially updates a classificationNom.
     *
     * @param classificationNom the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClassificationNom> partialUpdate(ClassificationNom classificationNom);

    /**
     * Get all the classificationNoms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassificationNom> findAll(Pageable pageable);

    /**
     * Get the "id" classificationNom.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassificationNom> findOne(Long id);

    /**
     * Delete the "id" classificationNom.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
