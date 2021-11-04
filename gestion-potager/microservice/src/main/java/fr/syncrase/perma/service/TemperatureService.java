package fr.syncrase.perma.service;

import fr.syncrase.perma.service.dto.TemperatureDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.syncrase.perma.domain.Temperature}.
 */
public interface TemperatureService {
    /**
     * Save a temperature.
     *
     * @param temperatureDTO the entity to save.
     * @return the persisted entity.
     */
    TemperatureDTO save(TemperatureDTO temperatureDTO);

    /**
     * Partially updates a temperature.
     *
     * @param temperatureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TemperatureDTO> partialUpdate(TemperatureDTO temperatureDTO);

    /**
     * Get all the temperatures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TemperatureDTO> findAll(Pageable pageable);

    /**
     * Get the "id" temperature.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TemperatureDTO> findOne(Long id);

    /**
     * Delete the "id" temperature.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
