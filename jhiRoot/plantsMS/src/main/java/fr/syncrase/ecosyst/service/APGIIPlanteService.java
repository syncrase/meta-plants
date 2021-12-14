package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.APGIIPlante;
import fr.syncrase.ecosyst.repository.APGIIPlanteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link APGIIPlante}.
 */
@Service
@Transactional
public class APGIIPlanteService {

    private final Logger log = LoggerFactory.getLogger(APGIIPlanteService.class);

    private final APGIIPlanteRepository aPGIIPlanteRepository;

    public APGIIPlanteService(APGIIPlanteRepository aPGIIPlanteRepository) {
        this.aPGIIPlanteRepository = aPGIIPlanteRepository;
    }

    /**
     * Save a aPGIIPlante.
     *
     * @param aPGIIPlante the entity to save.
     * @return the persisted entity.
     */
    public APGIIPlante save(APGIIPlante aPGIIPlante) {
        log.debug("Request to save APGIIPlante : {}", aPGIIPlante);
        return aPGIIPlanteRepository.save(aPGIIPlante);
    }

    /**
     * Partially update a aPGIIPlante.
     *
     * @param aPGIIPlante the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<APGIIPlante> partialUpdate(APGIIPlante aPGIIPlante) {
        log.debug("Request to partially update APGIIPlante : {}", aPGIIPlante);

        return aPGIIPlanteRepository
            .findById(aPGIIPlante.getId())
            .map(existingAPGIIPlante -> {
                if (aPGIIPlante.getOrdre() != null) {
                    existingAPGIIPlante.setOrdre(aPGIIPlante.getOrdre());
                }
                if (aPGIIPlante.getFamille() != null) {
                    existingAPGIIPlante.setFamille(aPGIIPlante.getFamille());
                }

                return existingAPGIIPlante;
            })
            .map(aPGIIPlanteRepository::save);
    }

    /**
     * Get all the aPGIIPlantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIIPlante> findAll(Pageable pageable) {
        log.debug("Request to get all APGIIPlantes");
        return aPGIIPlanteRepository.findAll(pageable);
    }

    /**
     * Get one aPGIIPlante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<APGIIPlante> findOne(Long id) {
        log.debug("Request to get APGIIPlante : {}", id);
        return aPGIIPlanteRepository.findById(id);
    }

    /**
     * Delete the aPGIIPlante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete APGIIPlante : {}", id);
        aPGIIPlanteRepository.deleteById(id);
    }
}
