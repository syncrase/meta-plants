package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Allelopathie;
import fr.syncrase.ecosyst.repository.AllelopathieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Allelopathie}.
 */
@Service
@Transactional
public class AllelopathieService {

    private final Logger log = LoggerFactory.getLogger(AllelopathieService.class);

    private final AllelopathieRepository allelopathieRepository;

    public AllelopathieService(AllelopathieRepository allelopathieRepository) {
        this.allelopathieRepository = allelopathieRepository;
    }

    /**
     * Save a allelopathie.
     *
     * @param allelopathie the entity to save.
     * @return the persisted entity.
     */
    public Mono<Allelopathie> save(Allelopathie allelopathie) {
        log.debug("Request to save Allelopathie : {}", allelopathie);
        return allelopathieRepository.save(allelopathie);
    }

    /**
     * Partially update a allelopathie.
     *
     * @param allelopathie the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Allelopathie> partialUpdate(Allelopathie allelopathie) {
        log.debug("Request to partially update Allelopathie : {}", allelopathie);

        return allelopathieRepository
            .findById(allelopathie.getId())
            .map(existingAllelopathie -> {
                if (allelopathie.getType() != null) {
                    existingAllelopathie.setType(allelopathie.getType());
                }
                if (allelopathie.getDescription() != null) {
                    existingAllelopathie.setDescription(allelopathie.getDescription());
                }
                if (allelopathie.getImpact() != null) {
                    existingAllelopathie.setImpact(allelopathie.getImpact());
                }

                return existingAllelopathie;
            })
            .flatMap(allelopathieRepository::save);
    }

    /**
     * Get all the allelopathies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Allelopathie> findAll(Pageable pageable) {
        log.debug("Request to get all Allelopathies");
        return allelopathieRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of allelopathies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return allelopathieRepository.count();
    }

    /**
     * Get one allelopathie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Allelopathie> findOne(Long id) {
        log.debug("Request to get Allelopathie : {}", id);
        return allelopathieRepository.findById(id);
    }

    /**
     * Delete the allelopathie by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Allelopathie : {}", id);
        return allelopathieRepository.deleteById(id);
    }
}
