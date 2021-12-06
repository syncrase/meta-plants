package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Germination;
import fr.syncrase.ecosyst.repository.GerminationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Germination}.
 */
@Service
@Transactional
public class GerminationService {

    private final Logger log = LoggerFactory.getLogger(GerminationService.class);

    private final GerminationRepository germinationRepository;

    public GerminationService(GerminationRepository germinationRepository) {
        this.germinationRepository = germinationRepository;
    }

    /**
     * Save a germination.
     *
     * @param germination the entity to save.
     * @return the persisted entity.
     */
    public Mono<Germination> save(Germination germination) {
        log.debug("Request to save Germination : {}", germination);
        return germinationRepository.save(germination);
    }

    /**
     * Partially update a germination.
     *
     * @param germination the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Germination> partialUpdate(Germination germination) {
        log.debug("Request to partially update Germination : {}", germination);

        return germinationRepository
            .findById(germination.getId())
            .map(existingGermination -> {
                if (germination.getTempsDeGermination() != null) {
                    existingGermination.setTempsDeGermination(germination.getTempsDeGermination());
                }
                if (germination.getConditionDeGermination() != null) {
                    existingGermination.setConditionDeGermination(germination.getConditionDeGermination());
                }

                return existingGermination;
            })
            .flatMap(germinationRepository::save);
    }

    /**
     * Get all the germinations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Germination> findAll(Pageable pageable) {
        log.debug("Request to get all Germinations");
        return germinationRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of germinations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return germinationRepository.count();
    }

    /**
     * Get one germination by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Germination> findOne(Long id) {
        log.debug("Request to get Germination : {}", id);
        return germinationRepository.findById(id);
    }

    /**
     * Delete the germination by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Germination : {}", id);
        return germinationRepository.deleteById(id);
    }
}
