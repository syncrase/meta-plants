package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SuperOrdre;
import fr.syncrase.ecosyst.repository.SuperOrdreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SuperOrdre}.
 */
@Service
@Transactional
public class SuperOrdreService {

    private final Logger log = LoggerFactory.getLogger(SuperOrdreService.class);

    private final SuperOrdreRepository superOrdreRepository;

    public SuperOrdreService(SuperOrdreRepository superOrdreRepository) {
        this.superOrdreRepository = superOrdreRepository;
    }

    /**
     * Save a superOrdre.
     *
     * @param superOrdre the entity to save.
     * @return the persisted entity.
     */
    public SuperOrdre save(SuperOrdre superOrdre) {
        log.debug("Request to save SuperOrdre : {}", superOrdre);
        return superOrdreRepository.save(superOrdre);
    }

    /**
     * Partially update a superOrdre.
     *
     * @param superOrdre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SuperOrdre> partialUpdate(SuperOrdre superOrdre) {
        log.debug("Request to partially update SuperOrdre : {}", superOrdre);

        return superOrdreRepository
            .findById(superOrdre.getId())
            .map(existingSuperOrdre -> {
                if (superOrdre.getNomFr() != null) {
                    existingSuperOrdre.setNomFr(superOrdre.getNomFr());
                }
                if (superOrdre.getNomLatin() != null) {
                    existingSuperOrdre.setNomLatin(superOrdre.getNomLatin());
                }

                return existingSuperOrdre;
            })
            .map(superOrdreRepository::save);
    }

    /**
     * Get all the superOrdres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SuperOrdre> findAll(Pageable pageable) {
        log.debug("Request to get all SuperOrdres");
        return superOrdreRepository.findAll(pageable);
    }

    /**
     * Get one superOrdre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SuperOrdre> findOne(Long id) {
        log.debug("Request to get SuperOrdre : {}", id);
        return superOrdreRepository.findById(id);
    }

    /**
     * Delete the superOrdre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SuperOrdre : {}", id);
        superOrdreRepository.deleteById(id);
    }
}
