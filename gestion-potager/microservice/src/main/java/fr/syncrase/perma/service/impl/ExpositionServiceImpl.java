package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Exposition;
import fr.syncrase.perma.repository.ExpositionRepository;
import fr.syncrase.perma.service.ExpositionService;
import fr.syncrase.perma.service.dto.ExpositionDTO;
import fr.syncrase.perma.service.mapper.ExpositionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Exposition}.
 */
@Service
@Transactional
public class ExpositionServiceImpl implements ExpositionService {

    private final Logger log = LoggerFactory.getLogger(ExpositionServiceImpl.class);

    private final ExpositionRepository expositionRepository;

    private final ExpositionMapper expositionMapper;

    public ExpositionServiceImpl(ExpositionRepository expositionRepository, ExpositionMapper expositionMapper) {
        this.expositionRepository = expositionRepository;
        this.expositionMapper = expositionMapper;
    }

    @Override
    public ExpositionDTO save(ExpositionDTO expositionDTO) {
        log.debug("Request to save Exposition : {}", expositionDTO);
        Exposition exposition = expositionMapper.toEntity(expositionDTO);
        exposition = expositionRepository.save(exposition);
        return expositionMapper.toDto(exposition);
    }

    @Override
    public Optional<ExpositionDTO> partialUpdate(ExpositionDTO expositionDTO) {
        log.debug("Request to partially update Exposition : {}", expositionDTO);

        return expositionRepository
            .findById(expositionDTO.getId())
            .map(existingExposition -> {
                expositionMapper.partialUpdate(existingExposition, expositionDTO);

                return existingExposition;
            })
            .map(expositionRepository::save)
            .map(expositionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpositionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Expositions");
        return expositionRepository.findAll(pageable).map(expositionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExpositionDTO> findOne(Long id) {
        log.debug("Request to get Exposition : {}", id);
        return expositionRepository.findById(id).map(expositionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exposition : {}", id);
        expositionRepository.deleteById(id);
    }
}
