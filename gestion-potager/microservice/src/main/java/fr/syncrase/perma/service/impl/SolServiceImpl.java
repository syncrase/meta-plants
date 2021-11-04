package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Sol;
import fr.syncrase.perma.repository.SolRepository;
import fr.syncrase.perma.service.SolService;
import fr.syncrase.perma.service.dto.SolDTO;
import fr.syncrase.perma.service.mapper.SolMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sol}.
 */
@Service
@Transactional
public class SolServiceImpl implements SolService {

    private final Logger log = LoggerFactory.getLogger(SolServiceImpl.class);

    private final SolRepository solRepository;

    private final SolMapper solMapper;

    public SolServiceImpl(SolRepository solRepository, SolMapper solMapper) {
        this.solRepository = solRepository;
        this.solMapper = solMapper;
    }

    @Override
    public SolDTO save(SolDTO solDTO) {
        log.debug("Request to save Sol : {}", solDTO);
        Sol sol = solMapper.toEntity(solDTO);
        sol = solRepository.save(sol);
        return solMapper.toDto(sol);
    }

    @Override
    public Optional<SolDTO> partialUpdate(SolDTO solDTO) {
        log.debug("Request to partially update Sol : {}", solDTO);

        return solRepository
            .findById(solDTO.getId())
            .map(existingSol -> {
                solMapper.partialUpdate(existingSol, solDTO);

                return existingSol;
            })
            .map(solRepository::save)
            .map(solMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sols");
        return solRepository.findAll(pageable).map(solMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SolDTO> findOne(Long id) {
        log.debug("Request to get Sol : {}", id);
        return solRepository.findById(id).map(solMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sol : {}", id);
        solRepository.deleteById(id);
    }
}
