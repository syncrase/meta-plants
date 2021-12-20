package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Url;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Url}.
 */
public interface UrlService {
    /**
     * Save a url.
     *
     * @param url the entity to save.
     * @return the persisted entity.
     */
    Url save(Url url);

    /**
     * Partially updates a url.
     *
     * @param url the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Url> partialUpdate(Url url);

    /**
     * Get all the urls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Url> findAll(Pageable pageable);

    /**
     * Get the "id" url.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Url> findOne(Long id);

    /**
     * Delete the "id" url.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
