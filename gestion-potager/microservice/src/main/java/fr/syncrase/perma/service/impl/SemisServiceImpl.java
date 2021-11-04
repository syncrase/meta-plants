package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Semis;
import fr.syncrase.perma.repository.SemisRepository;
import fr.syncrase.perma.service.SemisService;
import fr.syncrase.perma.service.dto.SemisDTO;
import fr.syncrase.perma.service.mapper.SemisMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Semis}.
 */
@Service
@Transactional
public class SemisServiceImpl implements SemisService {

    private final Logger log = LoggerFactory.getLogger(SemisServiceImpl.class);

    private final SemisRepository semisRepository;

    private final SemisMapper semisMapper;

    public SemisServiceImpl(SemisRepository semisRepository, SemisMapper semisMapper) {
        this.semisRepository = semisRepository;
        this.semisMapper = semisMapper;
    }

    @Override
    public SemisDTO save(SemisDTO semisDTO) {
        log.debug("Request to save Semis : {}", semisDTO);
        Semis semis = semisMapper.toEntity(semisDTO);
        semis = semisRepository.save(semis);
        return semisMapper.toDto(semis);
    }

    @Override
    public Optional<SemisDTO> partialUpdate(SemisDTO semisDTO) {
        log.debug("Request to partially update Semis : {}", semisDTO);

        return semisRepository
            .findById(semisDTO.getId())
            .map(existingSemis -> {
                semisMapper.partialUpdate(existingSemis, semisDTO);

                return existingSemis;
            })
            .map(semisRepository::save)
            .map(semisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SemisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Semis");
        return semisRepository.findAll(pageable).map(semisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SemisDTO> findOne(Long id) {
        log.debug("Request to get Semis : {}", id);
        return semisRepository.findById(id).map(semisMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Semis : {}", id);
        semisRepository.deleteById(id);
    }
}
