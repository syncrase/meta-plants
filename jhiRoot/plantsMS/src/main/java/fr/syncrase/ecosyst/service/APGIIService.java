package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.APGII;
import fr.syncrase.ecosyst.repository.APGIIRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link APGII}.
 */
@Service
@Transactional
public class APGIIService {

    private final Logger log = LoggerFactory.getLogger(APGIIService.class);

    private final APGIIRepository aPGIIRepository;

    public APGIIService(APGIIRepository aPGIIRepository) {
        this.aPGIIRepository = aPGIIRepository;
    }

    /**
     * Save a aPGII.
     *
     * @param aPGII the entity to save.
     * @return the persisted entity.
     */
    public APGII save(APGII aPGII) {
        log.debug("Request to save APGII : {}", aPGII);
        return aPGIIRepository.save(aPGII);
    }

    /**
     * Partially update a aPGII.
     *
     * @param aPGII the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<APGII> partialUpdate(APGII aPGII) {
        log.debug("Request to partially update APGII : {}", aPGII);

        return aPGIIRepository
            .findById(aPGII.getId())
            .map(existingAPGII -> {
                if (aPGII.getOrdre() != null) {
                    existingAPGII.setOrdre(aPGII.getOrdre());
                }
                if (aPGII.getFamille() != null) {
                    existingAPGII.setFamille(aPGII.getFamille());
                }

                return existingAPGII;
            })
            .map(aPGIIRepository::save);
    }

    /**
     * Get all the aPGIIS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<APGII> findAll(Pageable pageable) {
        log.debug("Request to get all APGIIS");
        return aPGIIRepository.findAll(pageable);
    }

    /**
     * Get one aPGII by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<APGII> findOne(Long id) {
        log.debug("Request to get APGII : {}", id);
        return aPGIIRepository.findById(id);
    }

    /**
     * Delete the aPGII by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete APGII : {}", id);
        aPGIIRepository.deleteById(id);
    }
}
