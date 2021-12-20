package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Racine;
import fr.syncrase.ecosyst.repository.RacineRepository;
import fr.syncrase.ecosyst.service.RacineService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Racine}.
 */
@Service
@Transactional
public class RacineServiceImpl implements RacineService {

    private final Logger log = LoggerFactory.getLogger(RacineServiceImpl.class);

    private final RacineRepository racineRepository;

    public RacineServiceImpl(RacineRepository racineRepository) {
        this.racineRepository = racineRepository;
    }

    @Override
    public Racine save(Racine racine) {
        log.debug("Request to save Racine : {}", racine);
        return racineRepository.save(racine);
    }

    @Override
    public Optional<Racine> partialUpdate(Racine racine) {
        log.debug("Request to partially update Racine : {}", racine);

        return racineRepository
            .findById(racine.getId())
            .map(existingRacine -> {
                if (racine.getType() != null) {
                    existingRacine.setType(racine.getType());
                }

                return existingRacine;
            })
            .map(racineRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Racine> findAll(Pageable pageable) {
        log.debug("Request to get all Racines");
        return racineRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Racine> findOne(Long id) {
        log.debug("Request to get Racine : {}", id);
        return racineRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Racine : {}", id);
        racineRepository.deleteById(id);
    }
}
