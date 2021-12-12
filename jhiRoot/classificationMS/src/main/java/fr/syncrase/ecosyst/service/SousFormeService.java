package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousForme;
import fr.syncrase.ecosyst.repository.SousFormeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousForme}.
 */
@Service
@Transactional
public class SousFormeService {

    private final Logger log = LoggerFactory.getLogger(SousFormeService.class);

    private final SousFormeRepository sousFormeRepository;

    public SousFormeService(SousFormeRepository sousFormeRepository) {
        this.sousFormeRepository = sousFormeRepository;
    }

    /**
     * Save a sousForme.
     *
     * @param sousForme the entity to save.
     * @return the persisted entity.
     */
    public SousForme save(SousForme sousForme) {
        log.debug("Request to save SousForme : {}", sousForme);
        return sousFormeRepository.save(sousForme);
    }

    /**
     * Partially update a sousForme.
     *
     * @param sousForme the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousForme> partialUpdate(SousForme sousForme) {
        log.debug("Request to partially update SousForme : {}", sousForme);

        return sousFormeRepository
            .findById(sousForme.getId())
            .map(existingSousForme -> {
                if (sousForme.getNomFr() != null) {
                    existingSousForme.setNomFr(sousForme.getNomFr());
                }
                if (sousForme.getNomLatin() != null) {
                    existingSousForme.setNomLatin(sousForme.getNomLatin());
                }

                return existingSousForme;
            })
            .map(sousFormeRepository::save);
    }

    /**
     * Get all the sousFormes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousForme> findAll(Pageable pageable) {
        log.debug("Request to get all SousFormes");
        return sousFormeRepository.findAll(pageable);
    }

    /**
     * Get one sousForme by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousForme> findOne(Long id) {
        log.debug("Request to get SousForme : {}", id);
        return sousFormeRepository.findById(id);
    }

    /**
     * Delete the sousForme by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousForme : {}", id);
        sousFormeRepository.deleteById(id);
    }
}
