package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Sol;
import fr.syncrase.ecosyst.repository.SolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Sol}.
 */
@Service
@Transactional
public class SolService {

    private final Logger log = LoggerFactory.getLogger(SolService.class);

    private final SolRepository solRepository;

    public SolService(SolRepository solRepository) {
        this.solRepository = solRepository;
    }

    /**
     * Save a sol.
     *
     * @param sol the entity to save.
     * @return the persisted entity.
     */
    public Mono<Sol> save(Sol sol) {
        log.debug("Request to save Sol : {}", sol);
        return solRepository.save(sol);
    }

    /**
     * Partially update a sol.
     *
     * @param sol the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Sol> partialUpdate(Sol sol) {
        log.debug("Request to partially update Sol : {}", sol);

        return solRepository
            .findById(sol.getId())
            .map(existingSol -> {
                if (sol.getPhMin() != null) {
                    existingSol.setPhMin(sol.getPhMin());
                }
                if (sol.getPhMax() != null) {
                    existingSol.setPhMax(sol.getPhMax());
                }
                if (sol.getType() != null) {
                    existingSol.setType(sol.getType());
                }
                if (sol.getRichesse() != null) {
                    existingSol.setRichesse(sol.getRichesse());
                }

                return existingSol;
            })
            .flatMap(solRepository::save);
    }

    /**
     * Get all the sols.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Sol> findAll(Pageable pageable) {
        log.debug("Request to get all Sols");
        return solRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of sols available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return solRepository.count();
    }

    /**
     * Get one sol by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Sol> findOne(Long id) {
        log.debug("Request to get Sol : {}", id);
        return solRepository.findById(id);
    }

    /**
     * Delete the sol by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Sol : {}", id);
        return solRepository.deleteById(id);
    }
}
