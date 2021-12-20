package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Semis;
import fr.syncrase.ecosyst.repository.SemisRepository;
import fr.syncrase.ecosyst.service.SemisService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Semis}.
 */
@Service
@Transactional
public class SemisServiceImpl implements SemisService {

    private final Logger log = LoggerFactory.getLogger(SemisServiceImpl.class);

    private final SemisRepository semisRepository;

    public SemisServiceImpl(SemisRepository semisRepository) {
        this.semisRepository = semisRepository;
    }

    @Override
    public Semis save(Semis semis) {
        log.debug("Request to save Semis : {}", semis);
        return semisRepository.save(semis);
    }

    @Override
    public Optional<Semis> partialUpdate(Semis semis) {
        log.debug("Request to partially update Semis : {}", semis);

        return semisRepository
            .findById(semis.getId())
            .map(existingSemis -> {
                return existingSemis;
            })
            .map(semisRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Semis> findAll(Pageable pageable) {
        log.debug("Request to get all Semis");
        return semisRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Semis> findOne(Long id) {
        log.debug("Request to get Semis : {}", id);
        return semisRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Semis : {}", id);
        semisRepository.deleteById(id);
    }
}
