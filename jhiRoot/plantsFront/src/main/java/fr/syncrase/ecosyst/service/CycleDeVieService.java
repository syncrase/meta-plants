package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.CycleDeVie;
import fr.syncrase.ecosyst.repository.CycleDeVieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link CycleDeVie}.
 */
@Service
@Transactional
public class CycleDeVieService {

    private final Logger log = LoggerFactory.getLogger(CycleDeVieService.class);

    private final CycleDeVieRepository cycleDeVieRepository;

    public CycleDeVieService(CycleDeVieRepository cycleDeVieRepository) {
        this.cycleDeVieRepository = cycleDeVieRepository;
    }

    /**
     * Save a cycleDeVie.
     *
     * @param cycleDeVie the entity to save.
     * @return the persisted entity.
     */
    public Mono<CycleDeVie> save(CycleDeVie cycleDeVie) {
        log.debug("Request to save CycleDeVie : {}", cycleDeVie);
        return cycleDeVieRepository.save(cycleDeVie);
    }

    /**
     * Partially update a cycleDeVie.
     *
     * @param cycleDeVie the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CycleDeVie> partialUpdate(CycleDeVie cycleDeVie) {
        log.debug("Request to partially update CycleDeVie : {}", cycleDeVie);

        return cycleDeVieRepository
            .findById(cycleDeVie.getId())
            .map(existingCycleDeVie -> {
                return existingCycleDeVie;
            })
            .flatMap(cycleDeVieRepository::save);
    }

    /**
     * Get all the cycleDeVies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CycleDeVie> findAll(Pageable pageable) {
        log.debug("Request to get all CycleDeVies");
        return cycleDeVieRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of cycleDeVies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return cycleDeVieRepository.count();
    }

    /**
     * Get one cycleDeVie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CycleDeVie> findOne(Long id) {
        log.debug("Request to get CycleDeVie : {}", id);
        return cycleDeVieRepository.findById(id);
    }

    /**
     * Delete the cycleDeVie by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CycleDeVie : {}", id);
        return cycleDeVieRepository.deleteById(id);
    }
}
