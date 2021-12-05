package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Temperature;
import fr.syncrase.perma.repository.TemperatureRepository;
import fr.syncrase.perma.service.TemperatureService;
import fr.syncrase.perma.service.dto.TemperatureDTO;
import fr.syncrase.perma.service.mapper.TemperatureMapper;
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

    private final TemperatureMapper temperatureMapper;

    public TemperatureServiceImpl(TemperatureRepository temperatureRepository, TemperatureMapper temperatureMapper) {
        this.temperatureRepository = temperatureRepository;
        this.temperatureMapper = temperatureMapper;
    }

    @Override
    public TemperatureDTO save(TemperatureDTO temperatureDTO) {
        log.debug("Request to save Temperature : {}", temperatureDTO);
        Temperature temperature = temperatureMapper.toEntity(temperatureDTO);
        temperature = temperatureRepository.save(temperature);
        return temperatureMapper.toDto(temperature);
    }

    @Override
    public Optional<TemperatureDTO> partialUpdate(TemperatureDTO temperatureDTO) {
        log.debug("Request to partially update Temperature : {}", temperatureDTO);

        return temperatureRepository
            .findById(temperatureDTO.getId())
            .map(existingTemperature -> {
                temperatureMapper.partialUpdate(existingTemperature, temperatureDTO);

                return existingTemperature;
            })
            .map(temperatureRepository::save)
            .map(temperatureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TemperatureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Temperatures");
        return temperatureRepository.findAll(pageable).map(temperatureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TemperatureDTO> findOne(Long id) {
        log.debug("Request to get Temperature : {}", id);
        return temperatureRepository.findById(id).map(temperatureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Temperature : {}", id);
        temperatureRepository.deleteById(id);
    }
}
