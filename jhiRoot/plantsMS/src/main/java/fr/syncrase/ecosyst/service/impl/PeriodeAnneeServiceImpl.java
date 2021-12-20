package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import fr.syncrase.ecosyst.repository.PeriodeAnneeRepository;
import fr.syncrase.ecosyst.service.PeriodeAnneeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PeriodeAnnee}.
 */
@Service
@Transactional
public class PeriodeAnneeServiceImpl implements PeriodeAnneeService {

    private final Logger log = LoggerFactory.getLogger(PeriodeAnneeServiceImpl.class);

    private final PeriodeAnneeRepository periodeAnneeRepository;

    public PeriodeAnneeServiceImpl(PeriodeAnneeRepository periodeAnneeRepository) {
        this.periodeAnneeRepository = periodeAnneeRepository;
    }

    @Override
    public PeriodeAnnee save(PeriodeAnnee periodeAnnee) {
        log.debug("Request to save PeriodeAnnee : {}", periodeAnnee);
        return periodeAnneeRepository.save(periodeAnnee);
    }

    @Override
    public Optional<PeriodeAnnee> partialUpdate(PeriodeAnnee periodeAnnee) {
        log.debug("Request to partially update PeriodeAnnee : {}", periodeAnnee);

        return periodeAnneeRepository
            .findById(periodeAnnee.getId())
            .map(existingPeriodeAnnee -> {
                return existingPeriodeAnnee;
            })
            .map(periodeAnneeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PeriodeAnnee> findAll(Pageable pageable) {
        log.debug("Request to get all PeriodeAnnees");
        return periodeAnneeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PeriodeAnnee> findOne(Long id) {
        log.debug("Request to get PeriodeAnnee : {}", id);
        return periodeAnneeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PeriodeAnnee : {}", id);
        periodeAnneeRepository.deleteById(id);
    }
}
