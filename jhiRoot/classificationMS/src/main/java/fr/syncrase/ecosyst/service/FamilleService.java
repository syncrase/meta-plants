package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Famille;
import fr.syncrase.ecosyst.repository.FamilleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Famille}.
 */
@Service
@Transactional
public class FamilleService {

    private final Logger log = LoggerFactory.getLogger(FamilleService.class);

    private final FamilleRepository familleRepository;

    public FamilleService(FamilleRepository familleRepository) {
        this.familleRepository = familleRepository;
    }

    /**
     * Save a famille.
     *
     * @param famille the entity to save.
     * @return the persisted entity.
     */
    public Famille save(Famille famille) {
        log.debug("Request to save Famille : {}", famille);
        return familleRepository.save(famille);
    }

    /**
     * Partially update a famille.
     *
     * @param famille the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Famille> partialUpdate(Famille famille) {
        log.debug("Request to partially update Famille : {}", famille);

        return familleRepository
            .findById(famille.getId())
            .map(existingFamille -> {
                if (famille.getNomFr() != null) {
                    existingFamille.setNomFr(famille.getNomFr());
                }
                if (famille.getNomLatin() != null) {
                    existingFamille.setNomLatin(famille.getNomLatin());
                }

                return existingFamille;
            })
            .map(familleRepository::save);
    }

    /**
     * Get all the familles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Famille> findAll(Pageable pageable) {
        log.debug("Request to get all Familles");
        return familleRepository.findAll(pageable);
    }

    /**
     * Get one famille by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Famille> findOne(Long id) {
        log.debug("Request to get Famille : {}", id);
        return familleRepository.findById(id);
    }

    /**
     * Delete the famille by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Famille : {}", id);
        familleRepository.deleteById(id);
    }
}
