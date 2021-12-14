package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Clade;
import fr.syncrase.ecosyst.repository.CladeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Clade}.
 */
@Service
@Transactional
public class CladeService {

    private final Logger log = LoggerFactory.getLogger(CladeService.class);

    private final CladeRepository cladeRepository;

    public CladeService(CladeRepository cladeRepository) {
        this.cladeRepository = cladeRepository;
    }

    /**
     * Save a clade.
     *
     * @param clade the entity to save.
     * @return the persisted entity.
     */
    public Clade save(Clade clade) {
        log.debug("Request to save Clade : {}", clade);
        return cladeRepository.save(clade);
    }

    /**
     * Partially update a clade.
     *
     * @param clade the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Clade> partialUpdate(Clade clade) {
        log.debug("Request to partially update Clade : {}", clade);

        return cladeRepository
            .findById(clade.getId())
            .map(existingClade -> {
                if (clade.getNom() != null) {
                    existingClade.setNom(clade.getNom());
                }

                return existingClade;
            })
            .map(cladeRepository::save);
    }

    /**
     * Get all the clades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Clade> findAll(Pageable pageable) {
        log.debug("Request to get all Clades");
        return cladeRepository.findAll(pageable);
    }

    /**
     * Get one clade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Clade> findOne(Long id) {
        log.debug("Request to get Clade : {}", id);
        return cladeRepository.findById(id);
    }

    /**
     * Delete the clade by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Clade : {}", id);
        cladeRepository.deleteById(id);
    }
}
