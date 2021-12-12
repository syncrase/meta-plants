package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousOrdre;
import fr.syncrase.ecosyst.repository.SousOrdreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousOrdre}.
 */
@Service
@Transactional
public class SousOrdreService {

    private final Logger log = LoggerFactory.getLogger(SousOrdreService.class);

    private final SousOrdreRepository sousOrdreRepository;

    public SousOrdreService(SousOrdreRepository sousOrdreRepository) {
        this.sousOrdreRepository = sousOrdreRepository;
    }

    /**
     * Save a sousOrdre.
     *
     * @param sousOrdre the entity to save.
     * @return the persisted entity.
     */
    public SousOrdre save(SousOrdre sousOrdre) {
        log.debug("Request to save SousOrdre : {}", sousOrdre);
        return sousOrdreRepository.save(sousOrdre);
    }

    /**
     * Partially update a sousOrdre.
     *
     * @param sousOrdre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousOrdre> partialUpdate(SousOrdre sousOrdre) {
        log.debug("Request to partially update SousOrdre : {}", sousOrdre);

        return sousOrdreRepository
            .findById(sousOrdre.getId())
            .map(existingSousOrdre -> {
                if (sousOrdre.getNomFr() != null) {
                    existingSousOrdre.setNomFr(sousOrdre.getNomFr());
                }
                if (sousOrdre.getNomLatin() != null) {
                    existingSousOrdre.setNomLatin(sousOrdre.getNomLatin());
                }

                return existingSousOrdre;
            })
            .map(sousOrdreRepository::save);
    }

    /**
     * Get all the sousOrdres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousOrdre> findAll(Pageable pageable) {
        log.debug("Request to get all SousOrdres");
        return sousOrdreRepository.findAll(pageable);
    }

    /**
     * Get one sousOrdre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousOrdre> findOne(Long id) {
        log.debug("Request to get SousOrdre : {}", id);
        return sousOrdreRepository.findById(id);
    }

    /**
     * Delete the sousOrdre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousOrdre : {}", id);
        sousOrdreRepository.deleteById(id);
    }
}
