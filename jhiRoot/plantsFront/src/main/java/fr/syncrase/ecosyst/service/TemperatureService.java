package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Temperature;
import fr.syncrase.ecosyst.repository.TemperatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Temperature}.
 */
@Service
@Transactional
public class TemperatureService {

    private final Logger log = LoggerFactory.getLogger(TemperatureService.class);

    private final TemperatureRepository temperatureRepository;

    public TemperatureService(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    /**
     * Save a temperature.
     *
     * @param temperature the entity to save.
     * @return the persisted entity.
     */
    public Mono<Temperature> save(Temperature temperature) {
        log.debug("Request to save Temperature : {}", temperature);
        return temperatureRepository.save(temperature);
    }

    /**
     * Partially update a temperature.
     *
     * @param temperature the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Temperature> partialUpdate(Temperature temperature) {
        log.debug("Request to partially update Temperature : {}", temperature);

        return temperatureRepository
            .findById(temperature.getId())
            .map(existingTemperature -> {
                if (temperature.getMin() != null) {
                    existingTemperature.setMin(temperature.getMin());
                }
                if (temperature.getMax() != null) {
                    existingTemperature.setMax(temperature.getMax());
                }
                if (temperature.getDescription() != null) {
                    existingTemperature.setDescription(temperature.getDescription());
                }
                if (temperature.getRusticite() != null) {
                    existingTemperature.setRusticite(temperature.getRusticite());
                }

                return existingTemperature;
            })
            .flatMap(temperatureRepository::save);
    }

    /**
     * Get all the temperatures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Temperature> findAll(Pageable pageable) {
        log.debug("Request to get all Temperatures");
        return temperatureRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of temperatures available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return temperatureRepository.count();
    }

    /**
     * Get one temperature by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Temperature> findOne(Long id) {
        log.debug("Request to get Temperature : {}", id);
        return temperatureRepository.findById(id);
    }

    /**
     * Delete the temperature by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Temperature : {}", id);
        return temperatureRepository.deleteById(id);
    }
}
