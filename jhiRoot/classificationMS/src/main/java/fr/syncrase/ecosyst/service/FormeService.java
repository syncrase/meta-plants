package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Forme;
import fr.syncrase.ecosyst.repository.FormeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Forme}.
 */
@Service
@Transactional
public class FormeService {

    private final Logger log = LoggerFactory.getLogger(FormeService.class);

    private final FormeRepository formeRepository;

    public FormeService(FormeRepository formeRepository) {
        this.formeRepository = formeRepository;
    }

    /**
     * Save a forme.
     *
     * @param forme the entity to save.
     * @return the persisted entity.
     */
    public Forme save(Forme forme) {
        log.debug("Request to save Forme : {}", forme);
        return formeRepository.save(forme);
    }

    /**
     * Partially update a forme.
     *
     * @param forme the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Forme> partialUpdate(Forme forme) {
        log.debug("Request to partially update Forme : {}", forme);

        return formeRepository
            .findById(forme.getId())
            .map(existingForme -> {
                if (forme.getNomFr() != null) {
                    existingForme.setNomFr(forme.getNomFr());
                }
                if (forme.getNomLatin() != null) {
                    existingForme.setNomLatin(forme.getNomLatin());
                }

                return existingForme;
            })
            .map(formeRepository::save);
    }

    /**
     * Get all the formes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Forme> findAll(Pageable pageable) {
        log.debug("Request to get all Formes");
        return formeRepository.findAll(pageable);
    }

    /**
     * Get one forme by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Forme> findOne(Long id) {
        log.debug("Request to get Forme : {}", id);
        return formeRepository.findById(id);
    }

    /**
     * Delete the forme by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Forme : {}", id);
        formeRepository.deleteById(id);
    }
}
