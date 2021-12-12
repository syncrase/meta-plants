package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.InfraClasse;
import fr.syncrase.ecosyst.repository.InfraClasseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InfraClasse}.
 */
@Service
@Transactional
public class InfraClasseService {

    private final Logger log = LoggerFactory.getLogger(InfraClasseService.class);

    private final InfraClasseRepository infraClasseRepository;

    public InfraClasseService(InfraClasseRepository infraClasseRepository) {
        this.infraClasseRepository = infraClasseRepository;
    }

    /**
     * Save a infraClasse.
     *
     * @param infraClasse the entity to save.
     * @return the persisted entity.
     */
    public InfraClasse save(InfraClasse infraClasse) {
        log.debug("Request to save InfraClasse : {}", infraClasse);
        return infraClasseRepository.save(infraClasse);
    }

    /**
     * Partially update a infraClasse.
     *
     * @param infraClasse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InfraClasse> partialUpdate(InfraClasse infraClasse) {
        log.debug("Request to partially update InfraClasse : {}", infraClasse);

        return infraClasseRepository
            .findById(infraClasse.getId())
            .map(existingInfraClasse -> {
                if (infraClasse.getNomFr() != null) {
                    existingInfraClasse.setNomFr(infraClasse.getNomFr());
                }
                if (infraClasse.getNomLatin() != null) {
                    existingInfraClasse.setNomLatin(infraClasse.getNomLatin());
                }

                return existingInfraClasse;
            })
            .map(infraClasseRepository::save);
    }

    /**
     * Get all the infraClasses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InfraClasse> findAll(Pageable pageable) {
        log.debug("Request to get all InfraClasses");
        return infraClasseRepository.findAll(pageable);
    }

    /**
     * Get one infraClasse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InfraClasse> findOne(Long id) {
        log.debug("Request to get InfraClasse : {}", id);
        return infraClasseRepository.findById(id);
    }

    /**
     * Delete the infraClasse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InfraClasse : {}", id);
        infraClasseRepository.deleteById(id);
    }
}
