package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Feuillage;
import fr.syncrase.ecosyst.repository.FeuillageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Feuillage}.
 */
@Service
@Transactional
public class FeuillageService {

    private final Logger log = LoggerFactory.getLogger(FeuillageService.class);

    private final FeuillageRepository feuillageRepository;

    public FeuillageService(FeuillageRepository feuillageRepository) {
        this.feuillageRepository = feuillageRepository;
    }

    /**
     * Save a feuillage.
     *
     * @param feuillage the entity to save.
     * @return the persisted entity.
     */
    public Mono<Feuillage> save(Feuillage feuillage) {
        log.debug("Request to save Feuillage : {}", feuillage);
        return feuillageRepository.save(feuillage);
    }

    /**
     * Partially update a feuillage.
     *
     * @param feuillage the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Feuillage> partialUpdate(Feuillage feuillage) {
        log.debug("Request to partially update Feuillage : {}", feuillage);

        return feuillageRepository
            .findById(feuillage.getId())
            .map(existingFeuillage -> {
                if (feuillage.getType() != null) {
                    existingFeuillage.setType(feuillage.getType());
                }

                return existingFeuillage;
            })
            .flatMap(feuillageRepository::save);
    }

    /**
     * Get all the feuillages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Feuillage> findAll(Pageable pageable) {
        log.debug("Request to get all Feuillages");
        return feuillageRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of feuillages available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return feuillageRepository.count();
    }

    /**
     * Get one feuillage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Feuillage> findOne(Long id) {
        log.debug("Request to get Feuillage : {}", id);
        return feuillageRepository.findById(id);
    }

    /**
     * Delete the feuillage by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Feuillage : {}", id);
        return feuillageRepository.deleteById(id);
    }
}
