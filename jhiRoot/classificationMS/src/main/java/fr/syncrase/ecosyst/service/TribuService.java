package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Tribu;
import fr.syncrase.ecosyst.repository.TribuRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tribu}.
 */
@Service
@Transactional
public class TribuService {

    private final Logger log = LoggerFactory.getLogger(TribuService.class);

    private final TribuRepository tribuRepository;

    public TribuService(TribuRepository tribuRepository) {
        this.tribuRepository = tribuRepository;
    }

    /**
     * Save a tribu.
     *
     * @param tribu the entity to save.
     * @return the persisted entity.
     */
    public Tribu save(Tribu tribu) {
        log.debug("Request to save Tribu : {}", tribu);
        return tribuRepository.save(tribu);
    }

    /**
     * Partially update a tribu.
     *
     * @param tribu the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tribu> partialUpdate(Tribu tribu) {
        log.debug("Request to partially update Tribu : {}", tribu);

        return tribuRepository
            .findById(tribu.getId())
            .map(existingTribu -> {
                if (tribu.getNomFr() != null) {
                    existingTribu.setNomFr(tribu.getNomFr());
                }
                if (tribu.getNomLatin() != null) {
                    existingTribu.setNomLatin(tribu.getNomLatin());
                }

                return existingTribu;
            })
            .map(tribuRepository::save);
    }

    /**
     * Get all the tribus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Tribu> findAll(Pageable pageable) {
        log.debug("Request to get all Tribus");
        return tribuRepository.findAll(pageable);
    }

    /**
     * Get one tribu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tribu> findOne(Long id) {
        log.debug("Request to get Tribu : {}", id);
        return tribuRepository.findById(id);
    }

    /**
     * Delete the tribu by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tribu : {}", id);
        tribuRepository.deleteById(id);
    }
}
