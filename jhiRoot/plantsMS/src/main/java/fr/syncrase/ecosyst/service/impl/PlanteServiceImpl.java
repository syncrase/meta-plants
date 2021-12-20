package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.PlanteRepository;
import fr.syncrase.ecosyst.service.PlanteService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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

    public PlanteServiceImpl(PlanteRepository planteRepository) {
        this.planteRepository = planteRepository;
    }

    @Override
    public Plante save(Plante plante) {
        log.debug("Request to save Plante : {}", plante);
        return planteRepository.save(plante);
    }

    @Override
    public Optional<Plante> partialUpdate(Plante plante) {
        log.debug("Request to partially update Plante : {}", plante);

        return planteRepository
            .findById(plante.getId())
            .map(existingPlante -> {
                if (plante.getEntretien() != null) {
                    existingPlante.setEntretien(plante.getEntretien());
                }
                if (plante.getHistoire() != null) {
                    existingPlante.setHistoire(plante.getHistoire());
                }
                if (plante.getVitesseCroissance() != null) {
                    existingPlante.setVitesseCroissance(plante.getVitesseCroissance());
                }
                if (plante.getExposition() != null) {
                    existingPlante.setExposition(plante.getExposition());
                }

                return existingPlante;
            })
            .map(planteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Plante> findAll(Pageable pageable) {
        log.debug("Request to get all Plantes");
        return planteRepository.findAll(pageable);
    }

    public Page<Plante> findAllWithEagerRelationships(Pageable pageable) {
        return planteRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the plantes where ClassificationCronquist is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Plante> findAllWhereClassificationCronquistIsNull() {
        log.debug("Request to get all plantes where ClassificationCronquist is null");
        return StreamSupport
            .stream(planteRepository.findAll().spliterator(), false)
            .filter(plante -> plante.getClassificationCronquist() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Plante> findOne(Long id) {
        log.debug("Request to get Plante : {}", id);
        return planteRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Plante : {}", id);
        planteRepository.deleteById(id);
    }
}
