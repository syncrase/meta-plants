package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import fr.syncrase.ecosyst.repository.PeriodeAnneeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link PeriodeAnnee}.
 */
@Service
@Transactional
public class PeriodeAnneeService {

    private final Logger log = LoggerFactory.getLogger(PeriodeAnneeService.class);

    private final PeriodeAnneeRepository periodeAnneeRepository;

    public PeriodeAnneeService(PeriodeAnneeRepository periodeAnneeRepository) {
        this.periodeAnneeRepository = periodeAnneeRepository;
    }

    /**
     * Save a periodeAnnee.
     *
     * @param periodeAnnee the entity to save.
     * @return the persisted entity.
     */
    public Mono<PeriodeAnnee> save(PeriodeAnnee periodeAnnee) {
        log.debug("Request to save PeriodeAnnee : {}", periodeAnnee);
        return periodeAnneeRepository.save(periodeAnnee);
    }

    /**
     * Partially update a periodeAnnee.
     *
     * @param periodeAnnee the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PeriodeAnnee> partialUpdate(PeriodeAnnee periodeAnnee) {
        log.debug("Request to partially update PeriodeAnnee : {}", periodeAnnee);

        return periodeAnneeRepository
            .findById(periodeAnnee.getId())
            .map(existingPeriodeAnnee -> {
                return existingPeriodeAnnee;
            })
            .flatMap(periodeAnneeRepository::save);
    }

    /**
     * Get all the periodeAnnees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PeriodeAnnee> findAll(Pageable pageable) {
        log.debug("Request to get all PeriodeAnnees");
        return periodeAnneeRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of periodeAnnees available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return periodeAnneeRepository.count();
    }

    /**
     * Get one periodeAnnee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PeriodeAnnee> findOne(Long id) {
        log.debug("Request to get PeriodeAnnee : {}", id);
        return periodeAnneeRepository.findById(id);
    }

    /**
     * Delete the periodeAnnee by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PeriodeAnnee : {}", id);
        return periodeAnneeRepository.deleteById(id);
    }
}
