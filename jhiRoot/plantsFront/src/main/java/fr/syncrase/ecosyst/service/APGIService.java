package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.APGI;
import fr.syncrase.ecosyst.repository.APGIRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link APGI}.
 */
@Service
@Transactional
public class APGIService {

    private final Logger log = LoggerFactory.getLogger(APGIService.class);

    private final APGIRepository aPGIRepository;

    public APGIService(APGIRepository aPGIRepository) {
        this.aPGIRepository = aPGIRepository;
    }

    /**
     * Save a aPGI.
     *
     * @param aPGI the entity to save.
     * @return the persisted entity.
     */
    public Mono<APGI> save(APGI aPGI) {
        log.debug("Request to save APGI : {}", aPGI);
        return aPGIRepository.save(aPGI);
    }

    /**
     * Partially update a aPGI.
     *
     * @param aPGI the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<APGI> partialUpdate(APGI aPGI) {
        log.debug("Request to partially update APGI : {}", aPGI);

        return aPGIRepository
            .findById(aPGI.getId())
            .map(existingAPGI -> {
                if (aPGI.getOrdre() != null) {
                    existingAPGI.setOrdre(aPGI.getOrdre());
                }
                if (aPGI.getFamille() != null) {
                    existingAPGI.setFamille(aPGI.getFamille());
                }

                return existingAPGI;
            })
            .flatMap(aPGIRepository::save);
    }

    /**
     * Get all the aPGIS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<APGI> findAll(Pageable pageable) {
        log.debug("Request to get all APGIS");
        return aPGIRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of aPGIS available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return aPGIRepository.count();
    }

    /**
     * Get one aPGI by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<APGI> findOne(Long id) {
        log.debug("Request to get APGI : {}", id);
        return aPGIRepository.findById(id);
    }

    /**
     * Delete the aPGI by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete APGI : {}", id);
        return aPGIRepository.deleteById(id);
    }
}
