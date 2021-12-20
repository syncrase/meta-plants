package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.CycleDeVie;
import fr.syncrase.ecosyst.repository.CycleDeVieRepository;
import fr.syncrase.ecosyst.service.CycleDeVieService;
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

    public CycleDeVieServiceImpl(CycleDeVieRepository cycleDeVieRepository) {
        this.cycleDeVieRepository = cycleDeVieRepository;
    }

    @Override
    public CycleDeVie save(CycleDeVie cycleDeVie) {
        log.debug("Request to save CycleDeVie : {}", cycleDeVie);
        return cycleDeVieRepository.save(cycleDeVie);
    }

    @Override
    public Optional<CycleDeVie> partialUpdate(CycleDeVie cycleDeVie) {
        log.debug("Request to partially update CycleDeVie : {}", cycleDeVie);

        return cycleDeVieRepository
            .findById(cycleDeVie.getId())
            .map(existingCycleDeVie -> {
                return existingCycleDeVie;
            })
            .map(cycleDeVieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CycleDeVie> findAll(Pageable pageable) {
        log.debug("Request to get all CycleDeVies");
        return cycleDeVieRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CycleDeVie> findOne(Long id) {
        log.debug("Request to get CycleDeVie : {}", id);
        return cycleDeVieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CycleDeVie : {}", id);
        cycleDeVieRepository.deleteById(id);
    }
}
