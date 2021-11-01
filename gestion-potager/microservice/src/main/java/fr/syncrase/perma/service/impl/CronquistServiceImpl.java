package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.service.CronquistService;
import fr.syncrase.perma.domain.Cronquist;
import fr.syncrase.perma.repository.CronquistRepository;
import fr.syncrase.perma.service.dto.CronquistDTO;
import fr.syncrase.perma.service.mapper.CronquistMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Cronquist}.
 */
@Service
@Transactional
public class CronquistServiceImpl implements CronquistService {

    private final Logger log = LoggerFactory.getLogger(CronquistServiceImpl.class);

    private final CronquistRepository cronquistRepository;

    private final CronquistMapper cronquistMapper;

    public CronquistServiceImpl(CronquistRepository cronquistRepository, CronquistMapper cronquistMapper) {
        this.cronquistRepository = cronquistRepository;
        this.cronquistMapper = cronquistMapper;
    }

    @Override
    public CronquistDTO save(CronquistDTO cronquistDTO) {
        log.debug("Request to save Cronquist : {}", cronquistDTO);
        Cronquist cronquist = cronquistMapper.toEntity(cronquistDTO);
        cronquist = cronquistRepository.save(cronquist);
        return cronquistMapper.toDto(cronquist);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CronquistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cronquists");
        return cronquistRepository.findAll(pageable)
            .map(cronquistMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CronquistDTO> findOne(Long id) {
        log.debug("Request to get Cronquist : {}", id);
        return cronquistRepository.findById(id)
            .map(cronquistMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cronquist : {}", id);
        cronquistRepository.deleteById(id);
    }
}
