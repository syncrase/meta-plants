package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.NomVernaculaire;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link NomVernaculaire}.
 */
public interface NomVernaculaireService {
    /**
     * Save a nomVernaculaire.
     *
     * @param nomVernaculaire the entity to save.
     * @return the persisted entity.
     */
    NomVernaculaire save(NomVernaculaire nomVernaculaire);

    /**
     * Partially updates a nomVernaculaire.
     *
     * @param nomVernaculaire the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NomVernaculaire> partialUpdate(NomVernaculaire nomVernaculaire);

    /**
     * Get all the nomVernaculaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NomVernaculaire> findAll(Pageable pageable);

    /**
     * Get the "id" nomVernaculaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NomVernaculaire> findOne(Long id);

    /**
     * Delete the "id" nomVernaculaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
