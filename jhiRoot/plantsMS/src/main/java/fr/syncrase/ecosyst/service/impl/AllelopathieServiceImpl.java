package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Allelopathie;
import fr.syncrase.ecosyst.repository.AllelopathieRepository;
import fr.syncrase.ecosyst.service.AllelopathieService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Allelopathie}.
 */
@Service
@Transactional
public class AllelopathieServiceImpl implements AllelopathieService {

    private final Logger log = LoggerFactory.getLogger(AllelopathieServiceImpl.class);

    private final AllelopathieRepository allelopathieRepository;

    public AllelopathieServiceImpl(AllelopathieRepository allelopathieRepository) {
        this.allelopathieRepository = allelopathieRepository;
    }

    @Override
    public Allelopathie save(Allelopathie allelopathie) {
        log.debug("Request to save Allelopathie : {}", allelopathie);
        return allelopathieRepository.save(allelopathie);
    }

    @Override
    public Optional<Allelopathie> partialUpdate(Allelopathie allelopathie) {
        log.debug("Request to partially update Allelopathie : {}", allelopathie);

        return allelopathieRepository
            .findById(allelopathie.getId())
            .map(existingAllelopathie -> {
                if (allelopathie.getType() != null) {
                    existingAllelopathie.setType(allelopathie.getType());
                }
                if (allelopathie.getDescription() != null) {
                    existingAllelopathie.setDescription(allelopathie.getDescription());
                }
                if (allelopathie.getImpact() != null) {
                    existingAllelopathie.setImpact(allelopathie.getImpact());
                }

                return existingAllelopathie;
            })
            .map(allelopathieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Allelopathie> findAll(Pageable pageable) {
        log.debug("Request to get all Allelopathies");
        return allelopathieRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Allelopathie> findOne(Long id) {
        log.debug("Request to get Allelopathie : {}", id);
        return allelopathieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Allelopathie : {}", id);
        allelopathieRepository.deleteById(id);
    }
}
