package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.APGIV;
import fr.syncrase.ecosyst.repository.APGIVRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link APGIV}.
 */
@Service
@Transactional
public class APGIVService {

    private final Logger log = LoggerFactory.getLogger(APGIVService.class);

    private final APGIVRepository aPGIVRepository;

    public APGIVService(APGIVRepository aPGIVRepository) {
        this.aPGIVRepository = aPGIVRepository;
    }

    /**
     * Save a aPGIV.
     *
     * @param aPGIV the entity to save.
     * @return the persisted entity.
     */
    public Mono<APGIV> save(APGIV aPGIV) {
        log.debug("Request to save APGIV : {}", aPGIV);
        return aPGIVRepository.save(aPGIV);
    }

    /**
     * Partially update a aPGIV.
     *
     * @param aPGIV the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<APGIV> partialUpdate(APGIV aPGIV) {
        log.debug("Request to partially update APGIV : {}", aPGIV);

        return aPGIVRepository
            .findById(aPGIV.getId())
            .map(existingAPGIV -> {
                if (aPGIV.getOrdre() != null) {
                    existingAPGIV.setOrdre(aPGIV.getOrdre());
                }
                if (aPGIV.getFamille() != null) {
                    existingAPGIV.setFamille(aPGIV.getFamille());
                }

                return existingAPGIV;
            })
            .flatMap(aPGIVRepository::save);
    }

    /**
     * Get all the aPGIVS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<APGIV> findAll(Pageable pageable) {
        log.debug("Request to get all APGIVS");
        return aPGIVRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of aPGIVS available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return aPGIVRepository.count();
    }

    /**
     * Get one aPGIV by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<APGIV> findOne(Long id) {
        log.debug("Request to get APGIV : {}", id);
        return aPGIVRepository.findById(id);
    }

    /**
     * Delete the aPGIV by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete APGIV : {}", id);
        return aPGIVRepository.deleteById(id);
    }
}
