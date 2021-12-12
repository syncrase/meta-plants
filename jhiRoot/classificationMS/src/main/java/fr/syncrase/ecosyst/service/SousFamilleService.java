package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousFamille;
import fr.syncrase.ecosyst.repository.SousFamilleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousFamille}.
 */
@Service
@Transactional
public class SousFamilleService {

    private final Logger log = LoggerFactory.getLogger(SousFamilleService.class);

    private final SousFamilleRepository sousFamilleRepository;

    public SousFamilleService(SousFamilleRepository sousFamilleRepository) {
        this.sousFamilleRepository = sousFamilleRepository;
    }

    /**
     * Save a sousFamille.
     *
     * @param sousFamille the entity to save.
     * @return the persisted entity.
     */
    public SousFamille save(SousFamille sousFamille) {
        log.debug("Request to save SousFamille : {}", sousFamille);
        return sousFamilleRepository.save(sousFamille);
    }

    /**
     * Partially update a sousFamille.
     *
     * @param sousFamille the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousFamille> partialUpdate(SousFamille sousFamille) {
        log.debug("Request to partially update SousFamille : {}", sousFamille);

        return sousFamilleRepository
            .findById(sousFamille.getId())
            .map(existingSousFamille -> {
                if (sousFamille.getNomFr() != null) {
                    existingSousFamille.setNomFr(sousFamille.getNomFr());
                }
                if (sousFamille.getNomLatin() != null) {
                    existingSousFamille.setNomLatin(sousFamille.getNomLatin());
                }

                return existingSousFamille;
            })
            .map(sousFamilleRepository::save);
    }

    /**
     * Get all the sousFamilles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousFamille> findAll(Pageable pageable) {
        log.debug("Request to get all SousFamilles");
        return sousFamilleRepository.findAll(pageable);
    }

    /**
     * Get one sousFamille by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousFamille> findOne(Long id) {
        log.debug("Request to get SousFamille : {}", id);
        return sousFamilleRepository.findById(id);
    }

    /**
     * Delete the sousFamille by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousFamille : {}", id);
        sousFamilleRepository.deleteById(id);
    }
}
