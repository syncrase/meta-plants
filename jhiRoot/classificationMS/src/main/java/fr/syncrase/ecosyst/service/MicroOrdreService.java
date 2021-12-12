package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.MicroOrdre;
import fr.syncrase.ecosyst.repository.MicroOrdreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MicroOrdre}.
 */
@Service
@Transactional
public class MicroOrdreService {

    private final Logger log = LoggerFactory.getLogger(MicroOrdreService.class);

    private final MicroOrdreRepository microOrdreRepository;

    public MicroOrdreService(MicroOrdreRepository microOrdreRepository) {
        this.microOrdreRepository = microOrdreRepository;
    }

    /**
     * Save a microOrdre.
     *
     * @param microOrdre the entity to save.
     * @return the persisted entity.
     */
    public MicroOrdre save(MicroOrdre microOrdre) {
        log.debug("Request to save MicroOrdre : {}", microOrdre);
        return microOrdreRepository.save(microOrdre);
    }

    /**
     * Partially update a microOrdre.
     *
     * @param microOrdre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MicroOrdre> partialUpdate(MicroOrdre microOrdre) {
        log.debug("Request to partially update MicroOrdre : {}", microOrdre);

        return microOrdreRepository
            .findById(microOrdre.getId())
            .map(existingMicroOrdre -> {
                if (microOrdre.getNomFr() != null) {
                    existingMicroOrdre.setNomFr(microOrdre.getNomFr());
                }
                if (microOrdre.getNomLatin() != null) {
                    existingMicroOrdre.setNomLatin(microOrdre.getNomLatin());
                }

                return existingMicroOrdre;
            })
            .map(microOrdreRepository::save);
    }

    /**
     * Get all the microOrdres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MicroOrdre> findAll(Pageable pageable) {
        log.debug("Request to get all MicroOrdres");
        return microOrdreRepository.findAll(pageable);
    }

    /**
     * Get one microOrdre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MicroOrdre> findOne(Long id) {
        log.debug("Request to get MicroOrdre : {}", id);
        return microOrdreRepository.findById(id);
    }

    /**
     * Delete the microOrdre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MicroOrdre : {}", id);
        microOrdreRepository.deleteById(id);
    }
}
