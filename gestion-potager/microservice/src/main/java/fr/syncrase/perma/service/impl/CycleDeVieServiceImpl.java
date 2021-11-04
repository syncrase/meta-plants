package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.CycleDeVie;
import fr.syncrase.perma.repository.CycleDeVieRepository;
import fr.syncrase.perma.service.CycleDeVieService;
import fr.syncrase.perma.service.dto.CycleDeVieDTO;
import fr.syncrase.perma.service.mapper.CycleDeVieMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CycleDeVie}.
 */
@Service
@Transactional
public class CycleDeVieServiceImpl implements CycleDeVieService {

    private final Logger log = LoggerFactory.getLogger(CycleDeVieServiceImpl.class);

    private final CycleDeVieRepository cycleDeVieRepository;

    private final CycleDeVieMapper cycleDeVieMapper;

    public CycleDeVieServiceImpl(CycleDeVieRepository cycleDeVieRepository, CycleDeVieMapper cycleDeVieMapper) {
        this.cycleDeVieRepository = cycleDeVieRepository;
        this.cycleDeVieMapper = cycleDeVieMapper;
    }

    @Override
    public CycleDeVieDTO save(CycleDeVieDTO cycleDeVieDTO) {
        log.debug("Request to save CycleDeVie : {}", cycleDeVieDTO);
        CycleDeVie cycleDeVie = cycleDeVieMapper.toEntity(cycleDeVieDTO);
        cycleDeVie = cycleDeVieRepository.save(cycleDeVie);
        return cycleDeVieMapper.toDto(cycleDeVie);
    }

    @Override
    public Optional<CycleDeVieDTO> partialUpdate(CycleDeVieDTO cycleDeVieDTO) {
        log.debug("Request to partially update CycleDeVie : {}", cycleDeVieDTO);

        return cycleDeVieRepository
            .findById(cycleDeVieDTO.getId())
            .map(existingCycleDeVie -> {
                cycleDeVieMapper.partialUpdate(existingCycleDeVie, cycleDeVieDTO);

                return existingCycleDeVie;
            })
            .map(cycleDeVieRepository::save)
            .map(cycleDeVieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CycleDeVieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CycleDeVies");
        return cycleDeVieRepository.findAll(pageable).map(cycleDeVieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CycleDeVieDTO> findOne(Long id) {
        log.debug("Request to get CycleDeVie : {}", id);
        return cycleDeVieRepository.findById(id).map(cycleDeVieMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CycleDeVie : {}", id);
        cycleDeVieRepository.deleteById(id);
    }
}
