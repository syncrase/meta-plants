package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousVariete;
import fr.syncrase.ecosyst.repository.SousVarieteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousVariete}.
 */
@Service
@Transactional
public class SousVarieteService {

    private final Logger log = LoggerFactory.getLogger(SousVarieteService.class);

    private final SousVarieteRepository sousVarieteRepository;

    public SousVarieteService(SousVarieteRepository sousVarieteRepository) {
        this.sousVarieteRepository = sousVarieteRepository;
    }

    /**
     * Save a sousVariete.
     *
     * @param sousVariete the entity to save.
     * @return the persisted entity.
     */
    public SousVariete save(SousVariete sousVariete) {
        log.debug("Request to save SousVariete : {}", sousVariete);
        return sousVarieteRepository.save(sousVariete);
    }

    /**
     * Partially update a sousVariete.
     *
     * @param sousVariete the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousVariete> partialUpdate(SousVariete sousVariete) {
        log.debug("Request to partially update SousVariete : {}", sousVariete);

        return sousVarieteRepository
            .findById(sousVariete.getId())
            .map(existingSousVariete -> {
                if (sousVariete.getNomFr() != null) {
                    existingSousVariete.setNomFr(sousVariete.getNomFr());
                }
                if (sousVariete.getNomLatin() != null) {
                    existingSousVariete.setNomLatin(sousVariete.getNomLatin());
                }

                return existingSousVariete;
            })
            .map(sousVarieteRepository::save);
    }

    /**
     * Get all the sousVarietes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousVariete> findAll(Pageable pageable) {
        log.debug("Request to get all SousVarietes");
        return sousVarieteRepository.findAll(pageable);
    }

    /**
     * Get one sousVariete by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousVariete> findOne(Long id) {
        log.debug("Request to get SousVariete : {}", id);
        return sousVarieteRepository.findById(id);
    }

    /**
     * Delete the sousVariete by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousVariete : {}", id);
        sousVarieteRepository.deleteById(id);
    }
}
