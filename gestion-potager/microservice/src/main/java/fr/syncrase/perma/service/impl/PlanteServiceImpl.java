package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.PlanteRepository;
import fr.syncrase.perma.service.PlanteService;
import fr.syncrase.perma.service.dto.PlanteDTO;
import fr.syncrase.perma.service.mapper.PlanteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Plante}.
 */
@Service
@Transactional
public class PlanteServiceImpl implements PlanteService {

    private final Logger log = LoggerFactory.getLogger(PlanteServiceImpl.class);

    private final PlanteRepository planteRepository;

    private final PlanteMapper planteMapper;

    public PlanteServiceImpl(PlanteRepository planteRepository, PlanteMapper planteMapper) {
        this.planteRepository = planteRepository;
        this.planteMapper = planteMapper;
    }

    @Override
    public PlanteDTO save(PlanteDTO planteDTO) {
        log.debug("Request to save Plante : {}", planteDTO);
        Plante plante = planteMapper.toEntity(planteDTO);
        plante = planteRepository.save(plante);
        return planteMapper.toDto(plante);
    }

    @Override
    public Optional<PlanteDTO> partialUpdate(PlanteDTO planteDTO) {
        log.debug("Request to partially update Plante : {}", planteDTO);

        return planteRepository
            .findById(planteDTO.getId())
            .map(existingPlante -> {
                planteMapper.partialUpdate(existingPlante, planteDTO);

                return existingPlante;
            })
            .map(planteRepository::save)
            .map(planteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Plantes");
        return planteRepository.findAll(pageable).map(planteMapper::toDto);
    }

    public Page<PlanteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return planteRepository.findAllWithEagerRelationships(pageable).map(planteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanteDTO> findOne(Long id) {
        log.debug("Request to get Plante : {}", id);
        return planteRepository.findOneWithEagerRelationships(id).map(planteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Plante : {}", id);
        planteRepository.deleteById(id);
    }
}
