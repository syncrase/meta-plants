package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Strate;
import fr.syncrase.ecosyst.repository.StrateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Strate}.
 */
@Service
@Transactional
public class StrateService {

    private final Logger log = LoggerFactory.getLogger(StrateService.class);

    private final StrateRepository strateRepository;

    public StrateService(StrateRepository strateRepository) {
        this.strateRepository = strateRepository;
    }

    /**
     * Save a strate.
     *
     * @param strate the entity to save.
     * @return the persisted entity.
     */
    public Mono<Strate> save(Strate strate) {
        log.debug("Request to save Strate : {}", strate);
        return strateRepository.save(strate);
    }

    /**
     * Partially update a strate.
     *
     * @param strate the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Strate> partialUpdate(Strate strate) {
        log.debug("Request to partially update Strate : {}", strate);

        return strateRepository
            .findById(strate.getId())
            .map(existingStrate -> {
                if (strate.getType() != null) {
                    existingStrate.setType(strate.getType());
                }

                return existingStrate;
            })
            .flatMap(strateRepository::save);
    }

    /**
     * Get all the strates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Strate> findAll(Pageable pageable) {
        log.debug("Request to get all Strates");
        return strateRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of strates available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return strateRepository.count();
    }

    /**
     * Get one strate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Strate> findOne(Long id) {
        log.debug("Request to get Strate : {}", id);
        return strateRepository.findById(id);
    }

    /**
     * Delete the strate by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Strate : {}", id);
        return strateRepository.deleteById(id);
    }
}
