package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Reproduction;
import fr.syncrase.ecosyst.repository.ReproductionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Reproduction}.
 */
@Service
@Transactional
public class ReproductionService {

    private final Logger log = LoggerFactory.getLogger(ReproductionService.class);

    private final ReproductionRepository reproductionRepository;

    public ReproductionService(ReproductionRepository reproductionRepository) {
        this.reproductionRepository = reproductionRepository;
    }

    /**
     * Save a reproduction.
     *
     * @param reproduction the entity to save.
     * @return the persisted entity.
     */
    public Mono<Reproduction> save(Reproduction reproduction) {
        log.debug("Request to save Reproduction : {}", reproduction);
        return reproductionRepository.save(reproduction);
    }

    /**
     * Partially update a reproduction.
     *
     * @param reproduction the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Reproduction> partialUpdate(Reproduction reproduction) {
        log.debug("Request to partially update Reproduction : {}", reproduction);

        return reproductionRepository
            .findById(reproduction.getId())
            .map(existingReproduction -> {
                if (reproduction.getVitesse() != null) {
                    existingReproduction.setVitesse(reproduction.getVitesse());
                }
                if (reproduction.getType() != null) {
                    existingReproduction.setType(reproduction.getType());
                }

                return existingReproduction;
            })
            .flatMap(reproductionRepository::save);
    }

    /**
     * Get all the reproductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Reproduction> findAll(Pageable pageable) {
        log.debug("Request to get all Reproductions");
        return reproductionRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of reproductions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reproductionRepository.count();
    }

    /**
     * Get one reproduction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Reproduction> findOne(Long id) {
        log.debug("Request to get Reproduction : {}", id);
        return reproductionRepository.findById(id);
    }

    /**
     * Delete the reproduction by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Reproduction : {}", id);
        return reproductionRepository.deleteById(id);
    }
}
