package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.APGIII;
import fr.syncrase.ecosyst.repository.APGIIIRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link APGIII}.
 */
@Service
@Transactional
public class APGIIIService {

    private final Logger log = LoggerFactory.getLogger(APGIIIService.class);

    private final APGIIIRepository aPGIIIRepository;

    public APGIIIService(APGIIIRepository aPGIIIRepository) {
        this.aPGIIIRepository = aPGIIIRepository;
    }

    /**
     * Save a aPGIII.
     *
     * @param aPGIII the entity to save.
     * @return the persisted entity.
     */
    public APGIII save(APGIII aPGIII) {
        log.debug("Request to save APGIII : {}", aPGIII);
        return aPGIIIRepository.save(aPGIII);
    }

    /**
     * Partially update a aPGIII.
     *
     * @param aPGIII the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<APGIII> partialUpdate(APGIII aPGIII) {
        log.debug("Request to partially update APGIII : {}", aPGIII);

        return aPGIIIRepository
            .findById(aPGIII.getId())
            .map(existingAPGIII -> {
                if (aPGIII.getOrdre() != null) {
                    existingAPGIII.setOrdre(aPGIII.getOrdre());
                }
                if (aPGIII.getFamille() != null) {
                    existingAPGIII.setFamille(aPGIII.getFamille());
                }

                return existingAPGIII;
            })
            .map(aPGIIIRepository::save);
    }

    /**
     * Get all the aPGIIIS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIII> findAll(Pageable pageable) {
        log.debug("Request to get all APGIIIS");
        return aPGIIIRepository.findAll(pageable);
    }

    /**
     * Get one aPGIII by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<APGIII> findOne(Long id) {
        log.debug("Request to get APGIII : {}", id);
        return aPGIIIRepository.findById(id);
    }

    /**
     * Delete the aPGIII by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete APGIII : {}", id);
        aPGIIIRepository.deleteById(id);
    }
}
