package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Variete;
import fr.syncrase.ecosyst.repository.VarieteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Variete}.
 */
@Service
@Transactional
public class VarieteService {

    private final Logger log = LoggerFactory.getLogger(VarieteService.class);

    private final VarieteRepository varieteRepository;

    public VarieteService(VarieteRepository varieteRepository) {
        this.varieteRepository = varieteRepository;
    }

    /**
     * Save a variete.
     *
     * @param variete the entity to save.
     * @return the persisted entity.
     */
    public Variete save(Variete variete) {
        log.debug("Request to save Variete : {}", variete);
        return varieteRepository.save(variete);
    }

    /**
     * Partially update a variete.
     *
     * @param variete the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Variete> partialUpdate(Variete variete) {
        log.debug("Request to partially update Variete : {}", variete);

        return varieteRepository
            .findById(variete.getId())
            .map(existingVariete -> {
                if (variete.getNomFr() != null) {
                    existingVariete.setNomFr(variete.getNomFr());
                }
                if (variete.getNomLatin() != null) {
                    existingVariete.setNomLatin(variete.getNomLatin());
                }

                return existingVariete;
            })
            .map(varieteRepository::save);
    }

    /**
     * Get all the varietes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Variete> findAll(Pageable pageable) {
        log.debug("Request to get all Varietes");
        return varieteRepository.findAll(pageable);
    }

    /**
     * Get one variete by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Variete> findOne(Long id) {
        log.debug("Request to get Variete : {}", id);
        return varieteRepository.findById(id);
    }

    /**
     * Delete the variete by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Variete : {}", id);
        varieteRepository.deleteById(id);
    }
}
