package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousEspece;
import fr.syncrase.ecosyst.repository.SousEspeceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousEspece}.
 */
@Service
@Transactional
public class SousEspeceService {

    private final Logger log = LoggerFactory.getLogger(SousEspeceService.class);

    private final SousEspeceRepository sousEspeceRepository;

    public SousEspeceService(SousEspeceRepository sousEspeceRepository) {
        this.sousEspeceRepository = sousEspeceRepository;
    }

    /**
     * Save a sousEspece.
     *
     * @param sousEspece the entity to save.
     * @return the persisted entity.
     */
    public SousEspece save(SousEspece sousEspece) {
        log.debug("Request to save SousEspece : {}", sousEspece);
        return sousEspeceRepository.save(sousEspece);
    }

    /**
     * Partially update a sousEspece.
     *
     * @param sousEspece the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousEspece> partialUpdate(SousEspece sousEspece) {
        log.debug("Request to partially update SousEspece : {}", sousEspece);

        return sousEspeceRepository
            .findById(sousEspece.getId())
            .map(existingSousEspece -> {
                if (sousEspece.getNomFr() != null) {
                    existingSousEspece.setNomFr(sousEspece.getNomFr());
                }
                if (sousEspece.getNomLatin() != null) {
                    existingSousEspece.setNomLatin(sousEspece.getNomLatin());
                }

                return existingSousEspece;
            })
            .map(sousEspeceRepository::save);
    }

    /**
     * Get all the sousEspeces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousEspece> findAll(Pageable pageable) {
        log.debug("Request to get all SousEspeces");
        return sousEspeceRepository.findAll(pageable);
    }

    /**
     * Get one sousEspece by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousEspece> findOne(Long id) {
        log.debug("Request to get SousEspece : {}", id);
        return sousEspeceRepository.findById(id);
    }

    /**
     * Delete the sousEspece by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousEspece : {}", id);
        sousEspeceRepository.deleteById(id);
    }
}
