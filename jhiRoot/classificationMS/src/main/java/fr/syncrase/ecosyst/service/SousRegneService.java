package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousRegne;
import fr.syncrase.ecosyst.repository.SousRegneRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousRegne}.
 */
@Service
@Transactional
public class SousRegneService {

    private final Logger log = LoggerFactory.getLogger(SousRegneService.class);

    private final SousRegneRepository sousRegneRepository;

    public SousRegneService(SousRegneRepository sousRegneRepository) {
        this.sousRegneRepository = sousRegneRepository;
    }

    /**
     * Save a sousRegne.
     *
     * @param sousRegne the entity to save.
     * @return the persisted entity.
     */
    public SousRegne save(SousRegne sousRegne) {
        log.debug("Request to save SousRegne : {}", sousRegne);
        return sousRegneRepository.save(sousRegne);
    }

    /**
     * Partially update a sousRegne.
     *
     * @param sousRegne the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousRegne> partialUpdate(SousRegne sousRegne) {
        log.debug("Request to partially update SousRegne : {}", sousRegne);

        return sousRegneRepository
            .findById(sousRegne.getId())
            .map(existingSousRegne -> {
                if (sousRegne.getNomFr() != null) {
                    existingSousRegne.setNomFr(sousRegne.getNomFr());
                }
                if (sousRegne.getNomLatin() != null) {
                    existingSousRegne.setNomLatin(sousRegne.getNomLatin());
                }

                return existingSousRegne;
            })
            .map(sousRegneRepository::save);
    }

    /**
     * Get all the sousRegnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousRegne> findAll(Pageable pageable) {
        log.debug("Request to get all SousRegnes");
        return sousRegneRepository.findAll(pageable);
    }

    /**
     * Get one sousRegne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousRegne> findOne(Long id) {
        log.debug("Request to get SousRegne : {}", id);
        return sousRegneRepository.findById(id);
    }

    /**
     * Delete the sousRegne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousRegne : {}", id);
        sousRegneRepository.deleteById(id);
    }
}
