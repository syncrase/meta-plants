package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.InfraRegne;
import fr.syncrase.ecosyst.repository.InfraRegneRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InfraRegne}.
 */
@Service
@Transactional
public class InfraRegneService {

    private final Logger log = LoggerFactory.getLogger(InfraRegneService.class);

    private final InfraRegneRepository infraRegneRepository;

    public InfraRegneService(InfraRegneRepository infraRegneRepository) {
        this.infraRegneRepository = infraRegneRepository;
    }

    /**
     * Save a infraRegne.
     *
     * @param infraRegne the entity to save.
     * @return the persisted entity.
     */
    public InfraRegne save(InfraRegne infraRegne) {
        log.debug("Request to save InfraRegne : {}", infraRegne);
        return infraRegneRepository.save(infraRegne);
    }

    /**
     * Partially update a infraRegne.
     *
     * @param infraRegne the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InfraRegne> partialUpdate(InfraRegne infraRegne) {
        log.debug("Request to partially update InfraRegne : {}", infraRegne);

        return infraRegneRepository
            .findById(infraRegne.getId())
            .map(existingInfraRegne -> {
                if (infraRegne.getNomFr() != null) {
                    existingInfraRegne.setNomFr(infraRegne.getNomFr());
                }
                if (infraRegne.getNomLatin() != null) {
                    existingInfraRegne.setNomLatin(infraRegne.getNomLatin());
                }

                return existingInfraRegne;
            })
            .map(infraRegneRepository::save);
    }

    /**
     * Get all the infraRegnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InfraRegne> findAll(Pageable pageable) {
        log.debug("Request to get all InfraRegnes");
        return infraRegneRepository.findAll(pageable);
    }

    /**
     * Get one infraRegne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InfraRegne> findOne(Long id) {
        log.debug("Request to get InfraRegne : {}", id);
        return infraRegneRepository.findById(id);
    }

    /**
     * Delete the infraRegne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InfraRegne : {}", id);
        infraRegneRepository.deleteById(id);
    }
}
