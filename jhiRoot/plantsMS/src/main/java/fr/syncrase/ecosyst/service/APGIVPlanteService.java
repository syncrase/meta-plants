package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.APGIVPlante;
import fr.syncrase.ecosyst.repository.APGIVPlanteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link APGIVPlante}.
 */
@Service
@Transactional
public class APGIVPlanteService {

    private final Logger log = LoggerFactory.getLogger(APGIVPlanteService.class);

    private final APGIVPlanteRepository aPGIVPlanteRepository;

    public APGIVPlanteService(APGIVPlanteRepository aPGIVPlanteRepository) {
        this.aPGIVPlanteRepository = aPGIVPlanteRepository;
    }

    /**
     * Save a aPGIVPlante.
     *
     * @param aPGIVPlante the entity to save.
     * @return the persisted entity.
     */
    public APGIVPlante save(APGIVPlante aPGIVPlante) {
        log.debug("Request to save APGIVPlante : {}", aPGIVPlante);
        return aPGIVPlanteRepository.save(aPGIVPlante);
    }

    /**
     * Partially update a aPGIVPlante.
     *
     * @param aPGIVPlante the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<APGIVPlante> partialUpdate(APGIVPlante aPGIVPlante) {
        log.debug("Request to partially update APGIVPlante : {}", aPGIVPlante);

        return aPGIVPlanteRepository
            .findById(aPGIVPlante.getId())
            .map(existingAPGIVPlante -> {
                if (aPGIVPlante.getOrdre() != null) {
                    existingAPGIVPlante.setOrdre(aPGIVPlante.getOrdre());
                }
                if (aPGIVPlante.getFamille() != null) {
                    existingAPGIVPlante.setFamille(aPGIVPlante.getFamille());
                }

                return existingAPGIVPlante;
            })
            .map(aPGIVPlanteRepository::save);
    }

    /**
     * Get all the aPGIVPlantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIVPlante> findAll(Pageable pageable) {
        log.debug("Request to get all APGIVPlantes");
        return aPGIVPlanteRepository.findAll(pageable);
    }

    /**
     * Get one aPGIVPlante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<APGIVPlante> findOne(Long id) {
        log.debug("Request to get APGIVPlante : {}", id);
        return aPGIVPlanteRepository.findById(id);
    }

    /**
     * Delete the aPGIVPlante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete APGIVPlante : {}", id);
        aPGIVPlanteRepository.deleteById(id);
    }
}
