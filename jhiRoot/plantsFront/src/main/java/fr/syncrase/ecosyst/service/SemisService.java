package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Semis;
import fr.syncrase.ecosyst.repository.SemisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Semis}.
 */
@Service
@Transactional
public class SemisService {

    private final Logger log = LoggerFactory.getLogger(SemisService.class);

    private final SemisRepository semisRepository;

    public SemisService(SemisRepository semisRepository) {
        this.semisRepository = semisRepository;
    }

    /**
     * Save a semis.
     *
     * @param semis the entity to save.
     * @return the persisted entity.
     */
    public Mono<Semis> save(Semis semis) {
        log.debug("Request to save Semis : {}", semis);
        return semisRepository.save(semis);
    }

    /**
     * Partially update a semis.
     *
     * @param semis the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Semis> partialUpdate(Semis semis) {
        log.debug("Request to partially update Semis : {}", semis);

        return semisRepository
            .findById(semis.getId())
            .map(existingSemis -> {
                return existingSemis;
            })
            .flatMap(semisRepository::save);
    }

    /**
     * Get all the semis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Semis> findAll(Pageable pageable) {
        log.debug("Request to get all Semis");
        return semisRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of semis available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return semisRepository.count();
    }

    /**
     * Get one semis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Semis> findOne(Long id) {
        log.debug("Request to get Semis : {}", id);
        return semisRepository.findById(id);
    }

    /**
     * Delete the semis by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Semis : {}", id);
        return semisRepository.deleteById(id);
    }
}
