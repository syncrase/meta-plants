package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SuperClasse;
import fr.syncrase.ecosyst.repository.SuperClasseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SuperClasse}.
 */
@Service
@Transactional
public class SuperClasseService {

    private final Logger log = LoggerFactory.getLogger(SuperClasseService.class);

    private final SuperClasseRepository superClasseRepository;

    public SuperClasseService(SuperClasseRepository superClasseRepository) {
        this.superClasseRepository = superClasseRepository;
    }

    /**
     * Save a superClasse.
     *
     * @param superClasse the entity to save.
     * @return the persisted entity.
     */
    public SuperClasse save(SuperClasse superClasse) {
        log.debug("Request to save SuperClasse : {}", superClasse);
        return superClasseRepository.save(superClasse);
    }

    /**
     * Partially update a superClasse.
     *
     * @param superClasse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SuperClasse> partialUpdate(SuperClasse superClasse) {
        log.debug("Request to partially update SuperClasse : {}", superClasse);

        return superClasseRepository
            .findById(superClasse.getId())
            .map(existingSuperClasse -> {
                if (superClasse.getNomFr() != null) {
                    existingSuperClasse.setNomFr(superClasse.getNomFr());
                }
                if (superClasse.getNomLatin() != null) {
                    existingSuperClasse.setNomLatin(superClasse.getNomLatin());
                }

                return existingSuperClasse;
            })
            .map(superClasseRepository::save);
    }

    /**
     * Get all the superClasses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SuperClasse> findAll(Pageable pageable) {
        log.debug("Request to get all SuperClasses");
        return superClasseRepository.findAll(pageable);
    }

    /**
     * Get one superClasse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SuperClasse> findOne(Long id) {
        log.debug("Request to get SuperClasse : {}", id);
        return superClasseRepository.findById(id);
    }

    /**
     * Delete the superClasse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SuperClasse : {}", id);
        superClasseRepository.deleteById(id);
    }
}
