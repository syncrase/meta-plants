package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Ressemblance;
import fr.syncrase.ecosyst.repository.RessemblanceRepository;
import fr.syncrase.ecosyst.service.RessemblanceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ressemblance}.
 */
@Service
@Transactional
public class RessemblanceServiceImpl implements RessemblanceService {

    private final Logger log = LoggerFactory.getLogger(RessemblanceServiceImpl.class);

    private final RessemblanceRepository ressemblanceRepository;

    public RessemblanceServiceImpl(RessemblanceRepository ressemblanceRepository) {
        this.ressemblanceRepository = ressemblanceRepository;
    }

    @Override
    public Ressemblance save(Ressemblance ressemblance) {
        log.debug("Request to save Ressemblance : {}", ressemblance);
        return ressemblanceRepository.save(ressemblance);
    }

    @Override
    public Optional<Ressemblance> partialUpdate(Ressemblance ressemblance) {
        log.debug("Request to partially update Ressemblance : {}", ressemblance);

        return ressemblanceRepository
            .findById(ressemblance.getId())
            .map(existingRessemblance -> {
                if (ressemblance.getDescription() != null) {
                    existingRessemblance.setDescription(ressemblance.getDescription());
                }

                return existingRessemblance;
            })
            .map(ressemblanceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ressemblance> findAll(Pageable pageable) {
        log.debug("Request to get all Ressemblances");
        return ressemblanceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ressemblance> findOne(Long id) {
        log.debug("Request to get Ressemblance : {}", id);
        return ressemblanceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ressemblance : {}", id);
        ressemblanceRepository.deleteById(id);
    }
}
