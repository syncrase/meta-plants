package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.InfraOrdre;
import fr.syncrase.ecosyst.repository.InfraOrdreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InfraOrdre}.
 */
@Service
@Transactional
public class InfraOrdreService {

    private final Logger log = LoggerFactory.getLogger(InfraOrdreService.class);

    private final InfraOrdreRepository infraOrdreRepository;

    public InfraOrdreService(InfraOrdreRepository infraOrdreRepository) {
        this.infraOrdreRepository = infraOrdreRepository;
    }

    /**
     * Save a infraOrdre.
     *
     * @param infraOrdre the entity to save.
     * @return the persisted entity.
     */
    public InfraOrdre save(InfraOrdre infraOrdre) {
        log.debug("Request to save InfraOrdre : {}", infraOrdre);
        return infraOrdreRepository.save(infraOrdre);
    }

    /**
     * Partially update a infraOrdre.
     *
     * @param infraOrdre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InfraOrdre> partialUpdate(InfraOrdre infraOrdre) {
        log.debug("Request to partially update InfraOrdre : {}", infraOrdre);

        return infraOrdreRepository
            .findById(infraOrdre.getId())
            .map(existingInfraOrdre -> {
                if (infraOrdre.getNomFr() != null) {
                    existingInfraOrdre.setNomFr(infraOrdre.getNomFr());
                }
                if (infraOrdre.getNomLatin() != null) {
                    existingInfraOrdre.setNomLatin(infraOrdre.getNomLatin());
                }

                return existingInfraOrdre;
            })
            .map(infraOrdreRepository::save);
    }

    /**
     * Get all the infraOrdres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InfraOrdre> findAll(Pageable pageable) {
        log.debug("Request to get all InfraOrdres");
        return infraOrdreRepository.findAll(pageable);
    }

    /**
     * Get one infraOrdre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InfraOrdre> findOne(Long id) {
        log.debug("Request to get InfraOrdre : {}", id);
        return infraOrdreRepository.findById(id);
    }

    /**
     * Delete the infraOrdre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InfraOrdre : {}", id);
        infraOrdreRepository.deleteById(id);
    }
}
