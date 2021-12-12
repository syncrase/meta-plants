package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousTribu;
import fr.syncrase.ecosyst.repository.SousTribuRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousTribu}.
 */
@Service
@Transactional
public class SousTribuService {

    private final Logger log = LoggerFactory.getLogger(SousTribuService.class);

    private final SousTribuRepository sousTribuRepository;

    public SousTribuService(SousTribuRepository sousTribuRepository) {
        this.sousTribuRepository = sousTribuRepository;
    }

    /**
     * Save a sousTribu.
     *
     * @param sousTribu the entity to save.
     * @return the persisted entity.
     */
    public SousTribu save(SousTribu sousTribu) {
        log.debug("Request to save SousTribu : {}", sousTribu);
        return sousTribuRepository.save(sousTribu);
    }

    /**
     * Partially update a sousTribu.
     *
     * @param sousTribu the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousTribu> partialUpdate(SousTribu sousTribu) {
        log.debug("Request to partially update SousTribu : {}", sousTribu);

        return sousTribuRepository
            .findById(sousTribu.getId())
            .map(existingSousTribu -> {
                if (sousTribu.getNomFr() != null) {
                    existingSousTribu.setNomFr(sousTribu.getNomFr());
                }
                if (sousTribu.getNomLatin() != null) {
                    existingSousTribu.setNomLatin(sousTribu.getNomLatin());
                }

                return existingSousTribu;
            })
            .map(sousTribuRepository::save);
    }

    /**
     * Get all the sousTribus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousTribu> findAll(Pageable pageable) {
        log.debug("Request to get all SousTribus");
        return sousTribuRepository.findAll(pageable);
    }

    /**
     * Get one sousTribu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousTribu> findOne(Long id) {
        log.debug("Request to get SousTribu : {}", id);
        return sousTribuRepository.findById(id);
    }

    /**
     * Delete the sousTribu by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousTribu : {}", id);
        sousTribuRepository.deleteById(id);
    }
}
