package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Regne;
import fr.syncrase.ecosyst.repository.RegneRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Regne}.
 */
@Service
@Transactional
public class RegneService {

    private final Logger log = LoggerFactory.getLogger(RegneService.class);

    private final RegneRepository regneRepository;

    public RegneService(RegneRepository regneRepository) {
        this.regneRepository = regneRepository;
    }

    /**
     * Save a regne.
     *
     * @param regne the entity to save.
     * @return the persisted entity.
     */
    public Regne save(Regne regne) {
        log.debug("Request to save Regne : {}", regne);
        return regneRepository.save(regne);
    }

    /**
     * Partially update a regne.
     *
     * @param regne the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Regne> partialUpdate(Regne regne) {
        log.debug("Request to partially update Regne : {}", regne);

        return regneRepository
            .findById(regne.getId())
            .map(existingRegne -> {
                if (regne.getNomFr() != null) {
                    existingRegne.setNomFr(regne.getNomFr());
                }
                if (regne.getNomLatin() != null) {
                    existingRegne.setNomLatin(regne.getNomLatin());
                }

                return existingRegne;
            })
            .map(regneRepository::save);
    }

    /**
     * Get all the regnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Regne> findAll(Pageable pageable) {
        log.debug("Request to get all Regnes");
        return regneRepository.findAll(pageable);
    }

    /**
     * Get one regne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Regne> findOne(Long id) {
        log.debug("Request to get Regne : {}", id);
        return regneRepository.findById(id);
    }

    /**
     * Delete the regne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Regne : {}", id);
        regneRepository.deleteById(id);
    }
}
