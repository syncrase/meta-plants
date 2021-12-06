package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Racine;
import fr.syncrase.ecosyst.repository.RacineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Racine}.
 */
@Service
@Transactional
public class RacineService {

    private final Logger log = LoggerFactory.getLogger(RacineService.class);

    private final RacineRepository racineRepository;

    public RacineService(RacineRepository racineRepository) {
        this.racineRepository = racineRepository;
    }

    /**
     * Save a racine.
     *
     * @param racine the entity to save.
     * @return the persisted entity.
     */
    public Mono<Racine> save(Racine racine) {
        log.debug("Request to save Racine : {}", racine);
        return racineRepository.save(racine);
    }

    /**
     * Partially update a racine.
     *
     * @param racine the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Racine> partialUpdate(Racine racine) {
        log.debug("Request to partially update Racine : {}", racine);

        return racineRepository
            .findById(racine.getId())
            .map(existingRacine -> {
                if (racine.getType() != null) {
                    existingRacine.setType(racine.getType());
                }

                return existingRacine;
            })
            .flatMap(racineRepository::save);
    }

    /**
     * Get all the racines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Racine> findAll(Pageable pageable) {
        log.debug("Request to get all Racines");
        return racineRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of racines available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return racineRepository.count();
    }

    /**
     * Get one racine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Racine> findOne(Long id) {
        log.debug("Request to get Racine : {}", id);
        return racineRepository.findById(id);
    }

    /**
     * Delete the racine by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Racine : {}", id);
        return racineRepository.deleteById(id);
    }
}
