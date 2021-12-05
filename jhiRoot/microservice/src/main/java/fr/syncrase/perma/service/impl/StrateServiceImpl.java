package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Strate;
import fr.syncrase.perma.repository.StrateRepository;
import fr.syncrase.perma.service.StrateService;
import fr.syncrase.perma.service.dto.StrateDTO;
import fr.syncrase.perma.service.mapper.StrateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Strate}.
 */
@Service
@Transactional
public class StrateServiceImpl implements StrateService {

    private final Logger log = LoggerFactory.getLogger(StrateServiceImpl.class);

    private final StrateRepository strateRepository;

    private final StrateMapper strateMapper;

    public StrateServiceImpl(StrateRepository strateRepository, StrateMapper strateMapper) {
        this.strateRepository = strateRepository;
        this.strateMapper = strateMapper;
    }

    @Override
    public StrateDTO save(StrateDTO strateDTO) {
        log.debug("Request to save Strate : {}", strateDTO);
        Strate strate = strateMapper.toEntity(strateDTO);
        strate = strateRepository.save(strate);
        return strateMapper.toDto(strate);
    }

    @Override
    public Optional<StrateDTO> partialUpdate(StrateDTO strateDTO) {
        log.debug("Request to partially update Strate : {}", strateDTO);

        return strateRepository
            .findById(strateDTO.getId())
            .map(existingStrate -> {
                strateMapper.partialUpdate(existingStrate, strateDTO);

                return existingStrate;
            })
            .map(strateRepository::save)
            .map(strateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StrateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Strates");
        return strateRepository.findAll(pageable).map(strateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StrateDTO> findOne(Long id) {
        log.debug("Request to get Strate : {}", id);
        return strateRepository.findById(id).map(strateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Strate : {}", id);
        strateRepository.deleteById(id);
    }
}
