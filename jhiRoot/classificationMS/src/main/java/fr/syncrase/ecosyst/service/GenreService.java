package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Genre;
import fr.syncrase.ecosyst.repository.GenreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Genre}.
 */
@Service
@Transactional
public class GenreService {

    private final Logger log = LoggerFactory.getLogger(GenreService.class);

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Save a genre.
     *
     * @param genre the entity to save.
     * @return the persisted entity.
     */
    public Genre save(Genre genre) {
        log.debug("Request to save Genre : {}", genre);
        return genreRepository.save(genre);
    }

    /**
     * Partially update a genre.
     *
     * @param genre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Genre> partialUpdate(Genre genre) {
        log.debug("Request to partially update Genre : {}", genre);

        return genreRepository
            .findById(genre.getId())
            .map(existingGenre -> {
                if (genre.getNomFr() != null) {
                    existingGenre.setNomFr(genre.getNomFr());
                }
                if (genre.getNomLatin() != null) {
                    existingGenre.setNomLatin(genre.getNomLatin());
                }

                return existingGenre;
            })
            .map(genreRepository::save);
    }

    /**
     * Get all the genres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Genre> findAll(Pageable pageable) {
        log.debug("Request to get all Genres");
        return genreRepository.findAll(pageable);
    }

    /**
     * Get one genre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Genre> findOne(Long id) {
        log.debug("Request to get Genre : {}", id);
        return genreRepository.findById(id);
    }

    /**
     * Delete the genre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Genre : {}", id);
        genreRepository.deleteById(id);
    }
}
