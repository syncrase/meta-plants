package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.service.RessemblanceService;
import fr.syncrase.perma.domain.Ressemblance;
import fr.syncrase.perma.repository.RessemblanceRepository;
import fr.syncrase.perma.service.dto.RessemblanceDTO;
import fr.syncrase.perma.service.mapper.RessemblanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Ressemblance}.
 */
@Service
@Transactional
public class RessemblanceServiceImpl implements RessemblanceService {

    private final Logger log = LoggerFactory.getLogger(RessemblanceServiceImpl.class);

    private final RessemblanceRepository ressemblanceRepository;

    private final RessemblanceMapper ressemblanceMapper;

    public RessemblanceServiceImpl(RessemblanceRepository ressemblanceRepository, RessemblanceMapper ressemblanceMapper) {
        this.ressemblanceRepository = ressemblanceRepository;
        this.ressemblanceMapper = ressemblanceMapper;
    }

    @Override
    public RessemblanceDTO save(RessemblanceDTO ressemblanceDTO) {
        log.debug("Request to save Ressemblance : {}", ressemblanceDTO);
        Ressemblance ressemblance = ressemblanceMapper.toEntity(ressemblanceDTO);
        ressemblance = ressemblanceRepository.save(ressemblance);
        return ressemblanceMapper.toDto(ressemblance);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RessemblanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ressemblances");
        return ressemblanceRepository.findAll(pageable)
            .map(ressemblanceMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RessemblanceDTO> findOne(Long id) {
        log.debug("Request to get Ressemblance : {}", id);
        return ressemblanceRepository.findById(id)
            .map(ressemblanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ressemblance : {}", id);
        ressemblanceRepository.deleteById(id);
    }
}
