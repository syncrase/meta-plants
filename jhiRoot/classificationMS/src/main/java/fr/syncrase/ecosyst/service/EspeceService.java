package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Espece;
import fr.syncrase.ecosyst.repository.EspeceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Espece}.
 */
@Service
@Transactional
public class EspeceService {

    private final Logger log = LoggerFactory.getLogger(EspeceService.class);

    private final EspeceRepository especeRepository;

    public EspeceService(EspeceRepository especeRepository) {
        this.especeRepository = especeRepository;
    }

    /**
     * Save a espece.
     *
     * @param espece the entity to save.
     * @return the persisted entity.
     */
    public Espece save(Espece espece) {
        log.debug("Request to save Espece : {}", espece);
        return especeRepository.save(espece);
    }

    /**
     * Partially update a espece.
     *
     * @param espece the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Espece> partialUpdate(Espece espece) {
        log.debug("Request to partially update Espece : {}", espece);

        return especeRepository
            .findById(espece.getId())
            .map(existingEspece -> {
                if (espece.getNomFr() != null) {
                    existingEspece.setNomFr(espece.getNomFr());
                }
                if (espece.getNomLatin() != null) {
                    existingEspece.setNomLatin(espece.getNomLatin());
                }

                return existingEspece;
            })
            .map(especeRepository::save);
    }

    /**
     * Get all the especes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Espece> findAll(Pageable pageable) {
        log.debug("Request to get all Especes");
        return especeRepository.findAll(pageable);
    }

    /**
     * Get one espece by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Espece> findOne(Long id) {
        log.debug("Request to get Espece : {}", id);
        return especeRepository.findById(id);
    }

    /**
     * Delete the espece by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Espece : {}", id);
        especeRepository.deleteById(id);
    }
}
