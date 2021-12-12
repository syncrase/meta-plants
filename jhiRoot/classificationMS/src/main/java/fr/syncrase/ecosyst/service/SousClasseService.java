package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousClasse;
import fr.syncrase.ecosyst.repository.SousClasseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousClasse}.
 */
@Service
@Transactional
public class SousClasseService {

    private final Logger log = LoggerFactory.getLogger(SousClasseService.class);

    private final SousClasseRepository sousClasseRepository;

    public SousClasseService(SousClasseRepository sousClasseRepository) {
        this.sousClasseRepository = sousClasseRepository;
    }

    /**
     * Save a sousClasse.
     *
     * @param sousClasse the entity to save.
     * @return the persisted entity.
     */
    public SousClasse save(SousClasse sousClasse) {
        log.debug("Request to save SousClasse : {}", sousClasse);
        return sousClasseRepository.save(sousClasse);
    }

    /**
     * Partially update a sousClasse.
     *
     * @param sousClasse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousClasse> partialUpdate(SousClasse sousClasse) {
        log.debug("Request to partially update SousClasse : {}", sousClasse);

        return sousClasseRepository
            .findById(sousClasse.getId())
            .map(existingSousClasse -> {
                if (sousClasse.getNomFr() != null) {
                    existingSousClasse.setNomFr(sousClasse.getNomFr());
                }
                if (sousClasse.getNomLatin() != null) {
                    existingSousClasse.setNomLatin(sousClasse.getNomLatin());
                }

                return existingSousClasse;
            })
            .map(sousClasseRepository::save);
    }

    /**
     * Get all the sousClasses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousClasse> findAll(Pageable pageable) {
        log.debug("Request to get all SousClasses");
        return sousClasseRepository.findAll(pageable);
    }

    /**
     * Get one sousClasse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousClasse> findOne(Long id) {
        log.debug("Request to get SousClasse : {}", id);
        return sousClasseRepository.findById(id);
    }

    /**
     * Delete the sousClasse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousClasse : {}", id);
        sousClasseRepository.deleteById(id);
    }
}
