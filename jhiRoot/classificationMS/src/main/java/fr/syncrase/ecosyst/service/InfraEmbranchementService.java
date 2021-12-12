package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.InfraEmbranchement;
import fr.syncrase.ecosyst.repository.InfraEmbranchementRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InfraEmbranchement}.
 */
@Service
@Transactional
public class InfraEmbranchementService {

    private final Logger log = LoggerFactory.getLogger(InfraEmbranchementService.class);

    private final InfraEmbranchementRepository infraEmbranchementRepository;

    public InfraEmbranchementService(InfraEmbranchementRepository infraEmbranchementRepository) {
        this.infraEmbranchementRepository = infraEmbranchementRepository;
    }

    /**
     * Save a infraEmbranchement.
     *
     * @param infraEmbranchement the entity to save.
     * @return the persisted entity.
     */
    public InfraEmbranchement save(InfraEmbranchement infraEmbranchement) {
        log.debug("Request to save InfraEmbranchement : {}", infraEmbranchement);
        return infraEmbranchementRepository.save(infraEmbranchement);
    }

    /**
     * Partially update a infraEmbranchement.
     *
     * @param infraEmbranchement the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InfraEmbranchement> partialUpdate(InfraEmbranchement infraEmbranchement) {
        log.debug("Request to partially update InfraEmbranchement : {}", infraEmbranchement);

        return infraEmbranchementRepository
            .findById(infraEmbranchement.getId())
            .map(existingInfraEmbranchement -> {
                if (infraEmbranchement.getNomFr() != null) {
                    existingInfraEmbranchement.setNomFr(infraEmbranchement.getNomFr());
                }
                if (infraEmbranchement.getNomLatin() != null) {
                    existingInfraEmbranchement.setNomLatin(infraEmbranchement.getNomLatin());
                }

                return existingInfraEmbranchement;
            })
            .map(infraEmbranchementRepository::save);
    }

    /**
     * Get all the infraEmbranchements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InfraEmbranchement> findAll(Pageable pageable) {
        log.debug("Request to get all InfraEmbranchements");
        return infraEmbranchementRepository.findAll(pageable);
    }

    /**
     * Get one infraEmbranchement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InfraEmbranchement> findOne(Long id) {
        log.debug("Request to get InfraEmbranchement : {}", id);
        return infraEmbranchementRepository.findById(id);
    }

    /**
     * Delete the infraEmbranchement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InfraEmbranchement : {}", id);
        infraEmbranchementRepository.deleteById(id);
    }
}
