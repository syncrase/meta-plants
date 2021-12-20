package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Temperature;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Temperature}.
 */
public interface TemperatureService {
    /**
     * Save a temperature.
     *
     * @param temperature the entity to save.
     * @return the persisted entity.
     */
    Temperature save(Temperature temperature);

    /**
     * Partially updates a temperature.
     *
     * @param temperature the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Temperature> partialUpdate(Temperature temperature);

    /**
     * Get all the temperatures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Temperature> findAll(Pageable pageable);

    /**
     * Get the "id" temperature.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Temperature> findOne(Long id);

    /**
     * Delete the "id" temperature.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
