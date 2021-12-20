package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Feuillage;
import fr.syncrase.ecosyst.repository.FeuillageRepository;
import fr.syncrase.ecosyst.service.FeuillageService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Feuillage}.
 */
@Service
@Transactional
public class FeuillageServiceImpl implements FeuillageService {

    private final Logger log = LoggerFactory.getLogger(FeuillageServiceImpl.class);

    private final FeuillageRepository feuillageRepository;

    public FeuillageServiceImpl(FeuillageRepository feuillageRepository) {
        this.feuillageRepository = feuillageRepository;
    }

    @Override
    public Feuillage save(Feuillage feuillage) {
        log.debug("Request to save Feuillage : {}", feuillage);
        return feuillageRepository.save(feuillage);
    }

    @Override
    public Optional<Feuillage> partialUpdate(Feuillage feuillage) {
        log.debug("Request to partially update Feuillage : {}", feuillage);

        return feuillageRepository
            .findById(feuillage.getId())
            .map(existingFeuillage -> {
                if (feuillage.getType() != null) {
                    existingFeuillage.setType(feuillage.getType());
                }

                return existingFeuillage;
            })
            .map(feuillageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Feuillage> findAll(Pageable pageable) {
        log.debug("Request to get all Feuillages");
        return feuillageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Feuillage> findOne(Long id) {
        log.debug("Request to get Feuillage : {}", id);
        return feuillageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Feuillage : {}", id);
        feuillageRepository.deleteById(id);
    }
}
