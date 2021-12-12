package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Rameau;
import fr.syncrase.ecosyst.repository.RameauRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rameau}.
 */
@Service
@Transactional
public class RameauService {

    private final Logger log = LoggerFactory.getLogger(RameauService.class);

    private final RameauRepository rameauRepository;

    public RameauService(RameauRepository rameauRepository) {
        this.rameauRepository = rameauRepository;
    }

    /**
     * Save a rameau.
     *
     * @param rameau the entity to save.
     * @return the persisted entity.
     */
    public Rameau save(Rameau rameau) {
        log.debug("Request to save Rameau : {}", rameau);
        return rameauRepository.save(rameau);
    }

    /**
     * Partially update a rameau.
     *
     * @param rameau the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Rameau> partialUpdate(Rameau rameau) {
        log.debug("Request to partially update Rameau : {}", rameau);

        return rameauRepository
            .findById(rameau.getId())
            .map(existingRameau -> {
                if (rameau.getNomFr() != null) {
                    existingRameau.setNomFr(rameau.getNomFr());
                }
                if (rameau.getNomLatin() != null) {
                    existingRameau.setNomLatin(rameau.getNomLatin());
                }

                return existingRameau;
            })
            .map(rameauRepository::save);
    }

    /**
     * Get all the rameaus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Rameau> findAll(Pageable pageable) {
        log.debug("Request to get all Rameaus");
        return rameauRepository.findAll(pageable);
    }

    /**
     * Get one rameau by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rameau> findOne(Long id) {
        log.debug("Request to get Rameau : {}", id);
        return rameauRepository.findById(id);
    }

    /**
     * Delete the rameau by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rameau : {}", id);
        rameauRepository.deleteById(id);
    }
}
