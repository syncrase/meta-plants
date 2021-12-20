package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Reproduction;
import fr.syncrase.ecosyst.repository.ReproductionRepository;
import fr.syncrase.ecosyst.service.ReproductionService;
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

    public ReproductionServiceImpl(ReproductionRepository reproductionRepository) {
        this.reproductionRepository = reproductionRepository;
    }

    @Override
    public Reproduction save(Reproduction reproduction) {
        log.debug("Request to save Reproduction : {}", reproduction);
        return reproductionRepository.save(reproduction);
    }

    @Override
    public Optional<Reproduction> partialUpdate(Reproduction reproduction) {
        log.debug("Request to partially update Reproduction : {}", reproduction);

        return reproductionRepository
            .findById(reproduction.getId())
            .map(existingReproduction -> {
                if (reproduction.getVitesse() != null) {
                    existingReproduction.setVitesse(reproduction.getVitesse());
                }
                if (reproduction.getType() != null) {
                    existingReproduction.setType(reproduction.getType());
                }

                return existingReproduction;
            })
            .map(reproductionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Reproduction> findAll(Pageable pageable) {
        log.debug("Request to get all Reproductions");
        return reproductionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reproduction> findOne(Long id) {
        log.debug("Request to get Reproduction : {}", id);
        return reproductionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reproduction : {}", id);
        reproductionRepository.deleteById(id);
    }
}
