package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.SousGenre;
import fr.syncrase.ecosyst.repository.SousGenreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SousGenre}.
 */
@Service
@Transactional
public class SousGenreService {

    private final Logger log = LoggerFactory.getLogger(SousGenreService.class);

    private final SousGenreRepository sousGenreRepository;

    public SousGenreService(SousGenreRepository sousGenreRepository) {
        this.sousGenreRepository = sousGenreRepository;
    }

    /**
     * Save a sousGenre.
     *
     * @param sousGenre the entity to save.
     * @return the persisted entity.
     */
    public SousGenre save(SousGenre sousGenre) {
        log.debug("Request to save SousGenre : {}", sousGenre);
        return sousGenreRepository.save(sousGenre);
    }

    /**
     * Partially update a sousGenre.
     *
     * @param sousGenre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousGenre> partialUpdate(SousGenre sousGenre) {
        log.debug("Request to partially update SousGenre : {}", sousGenre);

        return sousGenreRepository
            .findById(sousGenre.getId())
            .map(existingSousGenre -> {
                if (sousGenre.getNomFr() != null) {
                    existingSousGenre.setNomFr(sousGenre.getNomFr());
                }
                if (sousGenre.getNomLatin() != null) {
                    existingSousGenre.setNomLatin(sousGenre.getNomLatin());
                }

                return existingSousGenre;
            })
            .map(sousGenreRepository::save);
    }

    /**
     * Get all the sousGenres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousGenre> findAll(Pageable pageable) {
        log.debug("Request to get all SousGenres");
        return sousGenreRepository.findAll(pageable);
    }

    /**
     * Get one sousGenre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousGenre> findOne(Long id) {
        log.debug("Request to get SousGenre : {}", id);
        return sousGenreRepository.findById(id);
    }

    /**
     * Delete the sousGenre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousGenre : {}", id);
        sousGenreRepository.deleteById(id);
    }
}
