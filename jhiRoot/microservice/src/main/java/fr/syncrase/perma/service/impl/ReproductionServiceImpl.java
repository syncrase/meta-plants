package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Reproduction;
import fr.syncrase.perma.repository.ReproductionRepository;
import fr.syncrase.perma.service.ReproductionService;
import fr.syncrase.perma.service.dto.ReproductionDTO;
import fr.syncrase.perma.service.mapper.ReproductionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reproduction}.
 */
@Service
@Transactional
public class ReproductionServiceImpl implements ReproductionService {

    private final Logger log = LoggerFactory.getLogger(ReproductionServiceImpl.class);

    private final ReproductionRepository reproductionRepository;

    private final ReproductionMapper reproductionMapper;

    public ReproductionServiceImpl(ReproductionRepository reproductionRepository, ReproductionMapper reproductionMapper) {
        this.reproductionRepository = reproductionRepository;
        this.reproductionMapper = reproductionMapper;
    }

    @Override
    public ReproductionDTO save(ReproductionDTO reproductionDTO) {
        log.debug("Request to save Reproduction : {}", reproductionDTO);
        Reproduction reproduction = reproductionMapper.toEntity(reproductionDTO);
        reproduction = reproductionRepository.save(reproduction);
        return reproductionMapper.toDto(reproduction);
    }

    @Override
    public Optional<ReproductionDTO> partialUpdate(ReproductionDTO reproductionDTO) {
        log.debug("Request to partially update Reproduction : {}", reproductionDTO);

        return reproductionRepository
            .findById(reproductionDTO.getId())
            .map(existingReproduction -> {
                reproductionMapper.partialUpdate(existingReproduction, reproductionDTO);

                return existingReproduction;
            })
            .map(reproductionRepository::save)
            .map(reproductionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReproductionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reproductions");
        return reproductionRepository.findAll(pageable).map(reproductionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReproductionDTO> findOne(Long id) {
        log.debug("Request to get Reproduction : {}", id);
        return reproductionRepository.findById(id).map(reproductionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reproduction : {}", id);
        reproductionRepository.deleteById(id);
    }
}
