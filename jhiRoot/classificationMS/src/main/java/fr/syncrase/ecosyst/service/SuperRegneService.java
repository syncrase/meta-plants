package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SuperRegne;
import fr.syncrase.ecosyst.repository.SuperRegneRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SuperRegne}.
 */
@Service
@Transactional
public class SuperRegneService {

    private final Logger log = LoggerFactory.getLogger(SuperRegneService.class);

    private final SuperRegneRepository superRegneRepository;

    public SuperRegneService(SuperRegneRepository superRegneRepository) {
        this.superRegneRepository = superRegneRepository;
    }

    /**
     * Save a superRegne.
     *
     * @param superRegne the entity to save.
     * @return the persisted entity.
     */
    public SuperRegne save(SuperRegne superRegne) {
        log.debug("Request to save SuperRegne : {}", superRegne);
        return superRegneRepository.save(superRegne);
    }

    /**
     * Partially update a superRegne.
     *
     * @param superRegne the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SuperRegne> partialUpdate(SuperRegne superRegne) {
        log.debug("Request to partially update SuperRegne : {}", superRegne);

        return superRegneRepository
            .findById(superRegne.getId())
            .map(existingSuperRegne -> {
                if (superRegne.getNomFr() != null) {
                    existingSuperRegne.setNomFr(superRegne.getNomFr());
                }
                if (superRegne.getNomLatin() != null) {
                    existingSuperRegne.setNomLatin(superRegne.getNomLatin());
                }

                return existingSuperRegne;
            })
            .map(superRegneRepository::save);
    }

    /**
     * Get all the superRegnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SuperRegne> findAll(Pageable pageable) {
        log.debug("Request to get all SuperRegnes");
        return superRegneRepository.findAll(pageable);
    }

    /**
     * Get one superRegne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SuperRegne> findOne(Long id) {
        log.debug("Request to get SuperRegne : {}", id);
        return superRegneRepository.findById(id);
    }

    /**
     * Delete the superRegne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SuperRegne : {}", id);
        superRegneRepository.deleteById(id);
    }
}
