package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Sol;
import fr.syncrase.ecosyst.repository.SolRepository;
import fr.syncrase.ecosyst.service.SolService;
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

    public SolServiceImpl(SolRepository solRepository) {
        this.solRepository = solRepository;
    }

    @Override
    public Sol save(Sol sol) {
        log.debug("Request to save Sol : {}", sol);
        return solRepository.save(sol);
    }

    @Override
    public Optional<Sol> partialUpdate(Sol sol) {
        log.debug("Request to partially update Sol : {}", sol);

        return solRepository
            .findById(sol.getId())
            .map(existingSol -> {
                if (sol.getPhMin() != null) {
                    existingSol.setPhMin(sol.getPhMin());
                }
                if (sol.getPhMax() != null) {
                    existingSol.setPhMax(sol.getPhMax());
                }
                if (sol.getType() != null) {
                    existingSol.setType(sol.getType());
                }
                if (sol.getRichesse() != null) {
                    existingSol.setRichesse(sol.getRichesse());
                }

                return existingSol;
            })
            .map(solRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sol> findAll(Pageable pageable) {
        log.debug("Request to get all Sols");
        return solRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sol> findOne(Long id) {
        log.debug("Request to get Sol : {}", id);
        return solRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sol : {}", id);
        solRepository.deleteById(id);
    }
}
