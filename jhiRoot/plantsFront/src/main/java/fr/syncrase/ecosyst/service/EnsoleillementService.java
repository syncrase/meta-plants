package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Ensoleillement;
import fr.syncrase.ecosyst.repository.EnsoleillementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Ensoleillement}.
 */
@Service
@Transactional
public class EnsoleillementService {

    private final Logger log = LoggerFactory.getLogger(EnsoleillementService.class);

    private final EnsoleillementRepository ensoleillementRepository;

    public EnsoleillementService(EnsoleillementRepository ensoleillementRepository) {
        this.ensoleillementRepository = ensoleillementRepository;
    }

    /**
     * Save a ensoleillement.
     *
     * @param ensoleillement the entity to save.
     * @return the persisted entity.
     */
    public Mono<Ensoleillement> save(Ensoleillement ensoleillement) {
        log.debug("Request to save Ensoleillement : {}", ensoleillement);
        return ensoleillementRepository.save(ensoleillement);
    }

    /**
     * Partially update a ensoleillement.
     *
     * @param ensoleillement the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Ensoleillement> partialUpdate(Ensoleillement ensoleillement) {
        log.debug("Request to partially update Ensoleillement : {}", ensoleillement);

        return ensoleillementRepository
            .findById(ensoleillement.getId())
            .map(existingEnsoleillement -> {
                if (ensoleillement.getOrientation() != null) {
                    existingEnsoleillement.setOrientation(ensoleillement.getOrientation());
                }
                if (ensoleillement.getEnsoleilement() != null) {
                    existingEnsoleillement.setEnsoleilement(ensoleillement.getEnsoleilement());
                }

                return existingEnsoleillement;
            })
            .flatMap(ensoleillementRepository::save);
    }

    /**
     * Get all the ensoleillements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Ensoleillement> findAll(Pageable pageable) {
        log.debug("Request to get all Ensoleillements");
        return ensoleillementRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of ensoleillements available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return ensoleillementRepository.count();
    }

    /**
     * Get one ensoleillement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Ensoleillement> findOne(Long id) {
        log.debug("Request to get Ensoleillement : {}", id);
        return ensoleillementRepository.findById(id);
    }

    /**
     * Delete the ensoleillement by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Ensoleillement : {}", id);
        return ensoleillementRepository.deleteById(id);
    }
}
