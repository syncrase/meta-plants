package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Mois;
import fr.syncrase.ecosyst.repository.MoisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Mois}.
 */
@Service
@Transactional
public class MoisService {

    private final Logger log = LoggerFactory.getLogger(MoisService.class);

    private final MoisRepository moisRepository;

    public MoisService(MoisRepository moisRepository) {
        this.moisRepository = moisRepository;
    }

    /**
     * Save a mois.
     *
     * @param mois the entity to save.
     * @return the persisted entity.
     */
    public Mono<Mois> save(Mois mois) {
        log.debug("Request to save Mois : {}", mois);
        return moisRepository.save(mois);
    }

    /**
     * Partially update a mois.
     *
     * @param mois the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Mois> partialUpdate(Mois mois) {
        log.debug("Request to partially update Mois : {}", mois);

        return moisRepository
            .findById(mois.getId())
            .map(existingMois -> {
                if (mois.getNumero() != null) {
                    existingMois.setNumero(mois.getNumero());
                }
                if (mois.getNom() != null) {
                    existingMois.setNom(mois.getNom());
                }

                return existingMois;
            })
            .flatMap(moisRepository::save);
    }

    /**
     * Get all the mois.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Mois> findAll(Pageable pageable) {
        log.debug("Request to get all Mois");
        return moisRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of mois available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return moisRepository.count();
    }

    /**
     * Get one mois by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Mois> findOne(Long id) {
        log.debug("Request to get Mois : {}", id);
        return moisRepository.findById(id);
    }

    /**
     * Delete the mois by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Mois : {}", id);
        return moisRepository.deleteById(id);
    }
}
