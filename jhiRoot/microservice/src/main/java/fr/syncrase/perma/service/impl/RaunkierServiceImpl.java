package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Raunkier;
import fr.syncrase.perma.repository.RaunkierRepository;
import fr.syncrase.perma.service.RaunkierService;
import fr.syncrase.perma.service.dto.RaunkierDTO;
import fr.syncrase.perma.service.mapper.RaunkierMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Raunkier}.
 */
@Service
@Transactional
public class RaunkierServiceImpl implements RaunkierService {

    private final Logger log = LoggerFactory.getLogger(RaunkierServiceImpl.class);

    private final RaunkierRepository raunkierRepository;

    private final RaunkierMapper raunkierMapper;

    public RaunkierServiceImpl(RaunkierRepository raunkierRepository, RaunkierMapper raunkierMapper) {
        this.raunkierRepository = raunkierRepository;
        this.raunkierMapper = raunkierMapper;
    }

    @Override
    public RaunkierDTO save(RaunkierDTO raunkierDTO) {
        log.debug("Request to save Raunkier : {}", raunkierDTO);
        Raunkier raunkier = raunkierMapper.toEntity(raunkierDTO);
        raunkier = raunkierRepository.save(raunkier);
        return raunkierMapper.toDto(raunkier);
    }

    @Override
    public Optional<RaunkierDTO> partialUpdate(RaunkierDTO raunkierDTO) {
        log.debug("Request to partially update Raunkier : {}", raunkierDTO);

        return raunkierRepository
            .findById(raunkierDTO.getId())
            .map(existingRaunkier -> {
                raunkierMapper.partialUpdate(existingRaunkier, raunkierDTO);

                return existingRaunkier;
            })
            .map(raunkierRepository::save)
            .map(raunkierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RaunkierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Raunkiers");
        return raunkierRepository.findAll(pageable).map(raunkierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RaunkierDTO> findOne(Long id) {
        log.debug("Request to get Raunkier : {}", id);
        return raunkierRepository.findById(id).map(raunkierMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Raunkier : {}", id);
        raunkierRepository.deleteById(id);
    }
}
