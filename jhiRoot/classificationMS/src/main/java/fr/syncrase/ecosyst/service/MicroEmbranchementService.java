package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.MicroEmbranchement;
import fr.syncrase.ecosyst.repository.MicroEmbranchementRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MicroEmbranchement}.
 */
@Service
@Transactional
public class MicroEmbranchementService {

    private final Logger log = LoggerFactory.getLogger(MicroEmbranchementService.class);

    private final MicroEmbranchementRepository microEmbranchementRepository;

    public MicroEmbranchementService(MicroEmbranchementRepository microEmbranchementRepository) {
        this.microEmbranchementRepository = microEmbranchementRepository;
    }

    /**
     * Save a microEmbranchement.
     *
     * @param microEmbranchement the entity to save.
     * @return the persisted entity.
     */
    public MicroEmbranchement save(MicroEmbranchement microEmbranchement) {
        log.debug("Request to save MicroEmbranchement : {}", microEmbranchement);
        return microEmbranchementRepository.save(microEmbranchement);
    }

    /**
     * Partially update a microEmbranchement.
     *
     * @param microEmbranchement the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MicroEmbranchement> partialUpdate(MicroEmbranchement microEmbranchement) {
        log.debug("Request to partially update MicroEmbranchement : {}", microEmbranchement);

        return microEmbranchementRepository
            .findById(microEmbranchement.getId())
            .map(existingMicroEmbranchement -> {
                if (microEmbranchement.getNomFr() != null) {
                    existingMicroEmbranchement.setNomFr(microEmbranchement.getNomFr());
                }
                if (microEmbranchement.getNomLatin() != null) {
                    existingMicroEmbranchement.setNomLatin(microEmbranchement.getNomLatin());
                }

                return existingMicroEmbranchement;
            })
            .map(microEmbranchementRepository::save);
    }

    /**
     * Get all the microEmbranchements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MicroEmbranchement> findAll(Pageable pageable) {
        log.debug("Request to get all MicroEmbranchements");
        return microEmbranchementRepository.findAll(pageable);
    }

    /**
     * Get one microEmbranchement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MicroEmbranchement> findOne(Long id) {
        log.debug("Request to get MicroEmbranchement : {}", id);
        return microEmbranchementRepository.findById(id);
    }

    /**
     * Delete the microEmbranchement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MicroEmbranchement : {}", id);
        microEmbranchementRepository.deleteById(id);
    }
}
