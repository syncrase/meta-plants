package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SuperFamille;
import fr.syncrase.ecosyst.repository.SuperFamilleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SuperFamille}.
 */
@Service
@Transactional
public class SuperFamilleService {

    private final Logger log = LoggerFactory.getLogger(SuperFamilleService.class);

    private final SuperFamilleRepository superFamilleRepository;

    public SuperFamilleService(SuperFamilleRepository superFamilleRepository) {
        this.superFamilleRepository = superFamilleRepository;
    }

    /**
     * Save a superFamille.
     *
     * @param superFamille the entity to save.
     * @return the persisted entity.
     */
    public SuperFamille save(SuperFamille superFamille) {
        log.debug("Request to save SuperFamille : {}", superFamille);
        return superFamilleRepository.save(superFamille);
    }

    /**
     * Partially update a superFamille.
     *
     * @param superFamille the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SuperFamille> partialUpdate(SuperFamille superFamille) {
        log.debug("Request to partially update SuperFamille : {}", superFamille);

        return superFamilleRepository
            .findById(superFamille.getId())
            .map(existingSuperFamille -> {
                if (superFamille.getNomFr() != null) {
                    existingSuperFamille.setNomFr(superFamille.getNomFr());
                }
                if (superFamille.getNomLatin() != null) {
                    existingSuperFamille.setNomLatin(superFamille.getNomLatin());
                }

                return existingSuperFamille;
            })
            .map(superFamilleRepository::save);
    }

    /**
     * Get all the superFamilles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SuperFamille> findAll(Pageable pageable) {
        log.debug("Request to get all SuperFamilles");
        return superFamilleRepository.findAll(pageable);
    }

    /**
     * Get one superFamille by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SuperFamille> findOne(Long id) {
        log.debug("Request to get SuperFamille : {}", id);
        return superFamilleRepository.findById(id);
    }

    /**
     * Delete the superFamille by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SuperFamille : {}", id);
        superFamilleRepository.deleteById(id);
    }
}
