package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.NomVernaculaireDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.NomVernaculaire}.
 */
public interface NomVernaculaireService {

    /**
     * Save a nomVernaculaire.
     *
     * @param nomVernaculaireDTO the entity to save.
     * @return the persisted entity.
     */
    NomVernaculaireDTO save(NomVernaculaireDTO nomVernaculaireDTO);

    /**
     * Get all the nomVernaculaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NomVernaculaireDTO> findAll(Pageable pageable);


    /**
     * Get the "id" nomVernaculaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NomVernaculaireDTO> findOne(Long id);

    /**
     * Delete the "id" nomVernaculaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
