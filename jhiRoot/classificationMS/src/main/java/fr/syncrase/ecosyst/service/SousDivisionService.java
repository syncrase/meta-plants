package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousDivision;
import fr.syncrase.ecosyst.repository.SousDivisionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousDivision}.
 */
@Service
@Transactional
public class SousDivisionService {

    private final Logger log = LoggerFactory.getLogger(SousDivisionService.class);

    private final SousDivisionRepository sousDivisionRepository;

    public SousDivisionService(SousDivisionRepository sousDivisionRepository) {
        this.sousDivisionRepository = sousDivisionRepository;
    }

    /**
     * Save a sousDivision.
     *
     * @param sousDivision the entity to save.
     * @return the persisted entity.
     */
    public SousDivision save(SousDivision sousDivision) {
        log.debug("Request to save SousDivision : {}", sousDivision);
        return sousDivisionRepository.save(sousDivision);
    }

    /**
     * Partially update a sousDivision.
     *
     * @param sousDivision the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousDivision> partialUpdate(SousDivision sousDivision) {
        log.debug("Request to partially update SousDivision : {}", sousDivision);

        return sousDivisionRepository
            .findById(sousDivision.getId())
            .map(existingSousDivision -> {
                if (sousDivision.getNomFr() != null) {
                    existingSousDivision.setNomFr(sousDivision.getNomFr());
                }
                if (sousDivision.getNomLatin() != null) {
                    existingSousDivision.setNomLatin(sousDivision.getNomLatin());
                }

                return existingSousDivision;
            })
            .map(sousDivisionRepository::save);
    }

    /**
     * Get all the sousDivisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousDivision> findAll(Pageable pageable) {
        log.debug("Request to get all SousDivisions");
        return sousDivisionRepository.findAll(pageable);
    }

    /**
     * Get one sousDivision by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousDivision> findOne(Long id) {
        log.debug("Request to get SousDivision : {}", id);
        return sousDivisionRepository.findById(id);
    }

    /**
     * Delete the sousDivision by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousDivision : {}", id);
        sousDivisionRepository.deleteById(id);
    }
}
