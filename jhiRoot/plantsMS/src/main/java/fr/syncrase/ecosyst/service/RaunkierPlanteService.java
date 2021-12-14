package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.RaunkierPlante;
import fr.syncrase.ecosyst.repository.RaunkierPlanteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RaunkierPlante}.
 */
@Service
@Transactional
public class RaunkierPlanteService {

    private final Logger log = LoggerFactory.getLogger(RaunkierPlanteService.class);

    private final RaunkierPlanteRepository raunkierPlanteRepository;

    public RaunkierPlanteService(RaunkierPlanteRepository raunkierPlanteRepository) {
        this.raunkierPlanteRepository = raunkierPlanteRepository;
    }

    /**
     * Save a raunkierPlante.
     *
     * @param raunkierPlante the entity to save.
     * @return the persisted entity.
     */
    public RaunkierPlante save(RaunkierPlante raunkierPlante) {
        log.debug("Request to save RaunkierPlante : {}", raunkierPlante);
        return raunkierPlanteRepository.save(raunkierPlante);
    }

    /**
     * Partially update a raunkierPlante.
     *
     * @param raunkierPlante the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RaunkierPlante> partialUpdate(RaunkierPlante raunkierPlante) {
        log.debug("Request to partially update RaunkierPlante : {}", raunkierPlante);

        return raunkierPlanteRepository
            .findById(raunkierPlante.getId())
            .map(existingRaunkierPlante -> {
                if (raunkierPlante.getType() != null) {
                    existingRaunkierPlante.setType(raunkierPlante.getType());
                }

                return existingRaunkierPlante;
            })
            .map(raunkierPlanteRepository::save);
    }

    /**
     * Get all the raunkierPlantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RaunkierPlante> findAll(Pageable pageable) {
        log.debug("Request to get all RaunkierPlantes");
        return raunkierPlanteRepository.findAll(pageable);
    }

    /**
     * Get one raunkierPlante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RaunkierPlante> findOne(Long id) {
        log.debug("Request to get RaunkierPlante : {}", id);
        return raunkierPlanteRepository.findById(id);
    }

    /**
     * Delete the raunkierPlante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RaunkierPlante : {}", id);
        raunkierPlanteRepository.deleteById(id);
    }
}
