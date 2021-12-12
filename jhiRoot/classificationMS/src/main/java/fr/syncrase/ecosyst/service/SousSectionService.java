package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousSection;
import fr.syncrase.ecosyst.repository.SousSectionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousSection}.
 */
@Service
@Transactional
public class SousSectionService {

    private final Logger log = LoggerFactory.getLogger(SousSectionService.class);

    private final SousSectionRepository sousSectionRepository;

    public SousSectionService(SousSectionRepository sousSectionRepository) {
        this.sousSectionRepository = sousSectionRepository;
    }

    /**
     * Save a sousSection.
     *
     * @param sousSection the entity to save.
     * @return the persisted entity.
     */
    public SousSection save(SousSection sousSection) {
        log.debug("Request to save SousSection : {}", sousSection);
        return sousSectionRepository.save(sousSection);
    }

    /**
     * Partially update a sousSection.
     *
     * @param sousSection the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousSection> partialUpdate(SousSection sousSection) {
        log.debug("Request to partially update SousSection : {}", sousSection);

        return sousSectionRepository
            .findById(sousSection.getId())
            .map(existingSousSection -> {
                if (sousSection.getNomFr() != null) {
                    existingSousSection.setNomFr(sousSection.getNomFr());
                }
                if (sousSection.getNomLatin() != null) {
                    existingSousSection.setNomLatin(sousSection.getNomLatin());
                }

                return existingSousSection;
            })
            .map(sousSectionRepository::save);
    }

    /**
     * Get all the sousSections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousSection> findAll(Pageable pageable) {
        log.debug("Request to get all SousSections");
        return sousSectionRepository.findAll(pageable);
    }

    /**
     * Get one sousSection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousSection> findOne(Long id) {
        log.debug("Request to get SousSection : {}", id);
        return sousSectionRepository.findById(id);
    }

    /**
     * Delete the sousSection by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousSection : {}", id);
        sousSectionRepository.deleteById(id);
    }
}
