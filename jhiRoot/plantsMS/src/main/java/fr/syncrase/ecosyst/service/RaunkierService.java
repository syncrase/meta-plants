package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Raunkier;
import fr.syncrase.ecosyst.repository.RaunkierRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Raunkier}.
 */
@Service
@Transactional
public class RaunkierService {

    private final Logger log = LoggerFactory.getLogger(RaunkierService.class);

    private final RaunkierRepository raunkierRepository;

    public RaunkierService(RaunkierRepository raunkierRepository) {
        this.raunkierRepository = raunkierRepository;
    }

    /**
     * Save a raunkier.
     *
     * @param raunkier the entity to save.
     * @return the persisted entity.
     */
    public Raunkier save(Raunkier raunkier) {
        log.debug("Request to save Raunkier : {}", raunkier);
        return raunkierRepository.save(raunkier);
    }

    /**
     * Partially update a raunkier.
     *
     * @param raunkier the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Raunkier> partialUpdate(Raunkier raunkier) {
        log.debug("Request to partially update Raunkier : {}", raunkier);

        return raunkierRepository
            .findById(raunkier.getId())
            .map(existingRaunkier -> {
                if (raunkier.getType() != null) {
                    existingRaunkier.setType(raunkier.getType());
                }

                return existingRaunkier;
            })
            .map(raunkierRepository::save);
    }

    /**
     * Get all the raunkiers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Raunkier> findAll(Pageable pageable) {
        log.debug("Request to get all Raunkiers");
        return raunkierRepository.findAll(pageable);
    }

    /**
     * Get one raunkier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Raunkier> findOne(Long id) {
        log.debug("Request to get Raunkier : {}", id);
        return raunkierRepository.findById(id);
    }

    /**
     * Delete the raunkier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Raunkier : {}", id);
        raunkierRepository.deleteById(id);
    }
}
