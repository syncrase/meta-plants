package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.APGIPlante;
import fr.syncrase.ecosyst.repository.APGIPlanteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link APGIPlante}.
 */
@Service
@Transactional
public class APGIPlanteService {

    private final Logger log = LoggerFactory.getLogger(APGIPlanteService.class);

    private final APGIPlanteRepository aPGIPlanteRepository;

    public APGIPlanteService(APGIPlanteRepository aPGIPlanteRepository) {
        this.aPGIPlanteRepository = aPGIPlanteRepository;
    }

    /**
     * Save a aPGIPlante.
     *
     * @param aPGIPlante the entity to save.
     * @return the persisted entity.
     */
    public APGIPlante save(APGIPlante aPGIPlante) {
        log.debug("Request to save APGIPlante : {}", aPGIPlante);
        return aPGIPlanteRepository.save(aPGIPlante);
    }

    /**
     * Partially update a aPGIPlante.
     *
     * @param aPGIPlante the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<APGIPlante> partialUpdate(APGIPlante aPGIPlante) {
        log.debug("Request to partially update APGIPlante : {}", aPGIPlante);

        return aPGIPlanteRepository
            .findById(aPGIPlante.getId())
            .map(existingAPGIPlante -> {
                if (aPGIPlante.getOrdre() != null) {
                    existingAPGIPlante.setOrdre(aPGIPlante.getOrdre());
                }
                if (aPGIPlante.getFamille() != null) {
                    existingAPGIPlante.setFamille(aPGIPlante.getFamille());
                }

                return existingAPGIPlante;
            })
            .map(aPGIPlanteRepository::save);
    }

    /**
     * Get all the aPGIPlantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIPlante> findAll(Pageable pageable) {
        log.debug("Request to get all APGIPlantes");
        return aPGIPlanteRepository.findAll(pageable);
    }

    /**
     * Get one aPGIPlante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<APGIPlante> findOne(Long id) {
        log.debug("Request to get APGIPlante : {}", id);
        return aPGIPlanteRepository.findById(id);
    }

    /**
     * Delete the aPGIPlante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete APGIPlante : {}", id);
        aPGIPlanteRepository.deleteById(id);
    }
}
