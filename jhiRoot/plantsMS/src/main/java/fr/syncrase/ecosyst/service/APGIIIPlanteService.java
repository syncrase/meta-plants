package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.APGIIIPlante;
import fr.syncrase.ecosyst.repository.APGIIIPlanteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link APGIIIPlante}.
 */
@Service
@Transactional
public class APGIIIPlanteService {

    private final Logger log = LoggerFactory.getLogger(APGIIIPlanteService.class);

    private final APGIIIPlanteRepository aPGIIIPlanteRepository;

    public APGIIIPlanteService(APGIIIPlanteRepository aPGIIIPlanteRepository) {
        this.aPGIIIPlanteRepository = aPGIIIPlanteRepository;
    }

    /**
     * Save a aPGIIIPlante.
     *
     * @param aPGIIIPlante the entity to save.
     * @return the persisted entity.
     */
    public APGIIIPlante save(APGIIIPlante aPGIIIPlante) {
        log.debug("Request to save APGIIIPlante : {}", aPGIIIPlante);
        return aPGIIIPlanteRepository.save(aPGIIIPlante);
    }

    /**
     * Partially update a aPGIIIPlante.
     *
     * @param aPGIIIPlante the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<APGIIIPlante> partialUpdate(APGIIIPlante aPGIIIPlante) {
        log.debug("Request to partially update APGIIIPlante : {}", aPGIIIPlante);

        return aPGIIIPlanteRepository
            .findById(aPGIIIPlante.getId())
            .map(existingAPGIIIPlante -> {
                if (aPGIIIPlante.getOrdre() != null) {
                    existingAPGIIIPlante.setOrdre(aPGIIIPlante.getOrdre());
                }
                if (aPGIIIPlante.getFamille() != null) {
                    existingAPGIIIPlante.setFamille(aPGIIIPlante.getFamille());
                }
                if (aPGIIIPlante.getSousFamille() != null) {
                    existingAPGIIIPlante.setSousFamille(aPGIIIPlante.getSousFamille());
                }
                if (aPGIIIPlante.getTribu() != null) {
                    existingAPGIIIPlante.setTribu(aPGIIIPlante.getTribu());
                }
                if (aPGIIIPlante.getSousTribu() != null) {
                    existingAPGIIIPlante.setSousTribu(aPGIIIPlante.getSousTribu());
                }
                if (aPGIIIPlante.getGenre() != null) {
                    existingAPGIIIPlante.setGenre(aPGIIIPlante.getGenre());
                }

                return existingAPGIIIPlante;
            })
            .map(aPGIIIPlanteRepository::save);
    }

    /**
     * Get all the aPGIIIPlantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIIIPlante> findAll(Pageable pageable) {
        log.debug("Request to get all APGIIIPlantes");
        return aPGIIIPlanteRepository.findAll(pageable);
    }

    /**
     * Get all the aPGIIIPlantes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<APGIIIPlante> findAllWithEagerRelationships(Pageable pageable) {
        return aPGIIIPlanteRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one aPGIIIPlante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<APGIIIPlante> findOne(Long id) {
        log.debug("Request to get APGIIIPlante : {}", id);
        return aPGIIIPlanteRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the aPGIIIPlante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete APGIIIPlante : {}", id);
        aPGIIIPlanteRepository.deleteById(id);
    }
}
