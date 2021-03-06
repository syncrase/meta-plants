package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Temperature;
import fr.syncrase.ecosyst.repository.TemperatureRepository;
import fr.syncrase.ecosyst.service.TemperatureService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Temperature}.
 */
@Service
@Transactional
public class TemperatureServiceImpl implements TemperatureService {

    private final Logger log = LoggerFactory.getLogger(TemperatureServiceImpl.class);

    private final TemperatureRepository temperatureRepository;

    public TemperatureServiceImpl(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    @Override
    public Temperature save(Temperature temperature) {
        log.debug("Request to save Temperature : {}", temperature);
        return temperatureRepository.save(temperature);
    }

    @Override
    public Optional<Temperature> partialUpdate(Temperature temperature) {
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
            .map(temperatureRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Temperature> findAll(Pageable pageable) {
        log.debug("Request to get all Temperatures");
        return temperatureRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Temperature> findOne(Long id) {
        log.debug("Request to get Temperature : {}", id);
        return temperatureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Temperature : {}", id);
        temperatureRepository.deleteById(id);
    }
}
