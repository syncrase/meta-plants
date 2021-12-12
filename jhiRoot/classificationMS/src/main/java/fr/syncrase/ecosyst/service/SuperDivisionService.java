package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SuperDivision;
import fr.syncrase.ecosyst.repository.SuperDivisionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SuperDivision}.
 */
@Service
@Transactional
public class SuperDivisionService {

    private final Logger log = LoggerFactory.getLogger(SuperDivisionService.class);

    private final SuperDivisionRepository superDivisionRepository;

    public SuperDivisionService(SuperDivisionRepository superDivisionRepository) {
        this.superDivisionRepository = superDivisionRepository;
    }

    /**
     * Save a superDivision.
     *
     * @param superDivision the entity to save.
     * @return the persisted entity.
     */
    public SuperDivision save(SuperDivision superDivision) {
        log.debug("Request to save SuperDivision : {}", superDivision);
        return superDivisionRepository.save(superDivision);
    }

    /**
     * Partially update a superDivision.
     *
     * @param superDivision the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SuperDivision> partialUpdate(SuperDivision superDivision) {
        log.debug("Request to partially update SuperDivision : {}", superDivision);

        return superDivisionRepository
            .findById(superDivision.getId())
            .map(existingSuperDivision -> {
                if (superDivision.getNomFr() != null) {
                    existingSuperDivision.setNomFr(superDivision.getNomFr());
                }
                if (superDivision.getNomLatin() != null) {
                    existingSuperDivision.setNomLatin(superDivision.getNomLatin());
                }

                return existingSuperDivision;
            })
            .map(superDivisionRepository::save);
    }

    /**
     * Get all the superDivisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SuperDivision> findAll(Pageable pageable) {
        log.debug("Request to get all SuperDivisions");
        return superDivisionRepository.findAll(pageable);
    }

    /**
     * Get one superDivision by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SuperDivision> findOne(Long id) {
        log.debug("Request to get SuperDivision : {}", id);
        return superDivisionRepository.findById(id);
    }

    /**
     * Delete the superDivision by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SuperDivision : {}", id);
        superDivisionRepository.deleteById(id);
    }
}
