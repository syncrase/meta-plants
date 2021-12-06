package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Ressemblance;
import fr.syncrase.ecosyst.repository.RessemblanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Ressemblance}.
 */
@Service
@Transactional
public class RessemblanceService {

    private final Logger log = LoggerFactory.getLogger(RessemblanceService.class);

    private final RessemblanceRepository ressemblanceRepository;

    public RessemblanceService(RessemblanceRepository ressemblanceRepository) {
        this.ressemblanceRepository = ressemblanceRepository;
    }

    /**
     * Save a ressemblance.
     *
     * @param ressemblance the entity to save.
     * @return the persisted entity.
     */
    public Mono<Ressemblance> save(Ressemblance ressemblance) {
        log.debug("Request to save Ressemblance : {}", ressemblance);
        return ressemblanceRepository.save(ressemblance);
    }

    /**
     * Partially update a ressemblance.
     *
     * @param ressemblance the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Ressemblance> partialUpdate(Ressemblance ressemblance) {
        log.debug("Request to partially update Ressemblance : {}", ressemblance);

        return ressemblanceRepository
            .findById(ressemblance.getId())
            .map(existingRessemblance -> {
                if (ressemblance.getDescription() != null) {
                    existingRessemblance.setDescription(ressemblance.getDescription());
                }

                return existingRessemblance;
            })
            .flatMap(ressemblanceRepository::save);
    }

    /**
     * Get all the ressemblances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Ressemblance> findAll(Pageable pageable) {
        log.debug("Request to get all Ressemblances");
        return ressemblanceRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of ressemblances available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return ressemblanceRepository.count();
    }

    /**
     * Get one ressemblance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Ressemblance> findOne(Long id) {
        log.debug("Request to get Ressemblance : {}", id);
        return ressemblanceRepository.findById(id);
    }

    /**
     * Delete the ressemblance by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Ressemblance : {}", id);
        return ressemblanceRepository.deleteById(id);
    }
}
