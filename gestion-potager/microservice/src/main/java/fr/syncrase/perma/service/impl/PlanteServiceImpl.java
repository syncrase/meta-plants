package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.service.PlanteService;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.PlanteRepository;
import fr.syncrase.perma.service.dto.PlanteDTO;
import fr.syncrase.perma.service.mapper.PlanteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    @Transactional(readOnly = true)
    public Page<PlanteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Plantes");
        return planteRepository.findAll(pageable)
            .map(planteMapper::toDto);
    }


    public Page<PlanteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return planteRepository.findAllWithEagerRelationships(pageable).map(planteMapper::toDto);
    }


    /**
     *  Get all the plantes where AllelopathieRecue is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<PlanteDTO> findAllWhereAllelopathieRecueIsNull() {
        log.debug("Request to get all plantes where AllelopathieRecue is null");
        return StreamSupport
            .stream(planteRepository.findAll().spliterator(), false)
            .filter(plante -> plante.getAllelopathieRecue() == null)
            .map(planteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     *  Get all the plantes where AllelopathieProduite is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<PlanteDTO> findAllWhereAllelopathieProduiteIsNull() {
        log.debug("Request to get all plantes where AllelopathieProduite is null");
        return StreamSupport
            .stream(planteRepository.findAll().spliterator(), false)
            .filter(plante -> plante.getAllelopathieProduite() == null)
            .map(planteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanteDTO> findOne(Long id) {
        log.debug("Request to get Plante : {}", id);
        return planteRepository.findOneWithEagerRelationships(id)
            .map(planteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Plante : {}", id);
        planteRepository.deleteById(id);
    }
}
