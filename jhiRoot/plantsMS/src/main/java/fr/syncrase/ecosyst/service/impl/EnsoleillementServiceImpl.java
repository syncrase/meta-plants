package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Ensoleillement;
import fr.syncrase.ecosyst.repository.EnsoleillementRepository;
import fr.syncrase.ecosyst.service.EnsoleillementService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ensoleillement}.
 */
@Service
@Transactional
public class EnsoleillementServiceImpl implements EnsoleillementService {

    private final Logger log = LoggerFactory.getLogger(EnsoleillementServiceImpl.class);

    private final EnsoleillementRepository ensoleillementRepository;

    public EnsoleillementServiceImpl(EnsoleillementRepository ensoleillementRepository) {
        this.ensoleillementRepository = ensoleillementRepository;
    }

    @Override
    public Ensoleillement save(Ensoleillement ensoleillement) {
        log.debug("Request to save Ensoleillement : {}", ensoleillement);
        return ensoleillementRepository.save(ensoleillement);
    }

    @Override
    public Optional<Ensoleillement> partialUpdate(Ensoleillement ensoleillement) {
        log.debug("Request to partially update Ensoleillement : {}", ensoleillement);

        return ensoleillementRepository
            .findById(ensoleillement.getId())
            .map(existingEnsoleillement -> {
                if (ensoleillement.getOrientation() != null) {
                    existingEnsoleillement.setOrientation(ensoleillement.getOrientation());
                }
                if (ensoleillement.getEnsoleilement() != null) {
                    existingEnsoleillement.setEnsoleilement(ensoleillement.getEnsoleilement());
                }

                return existingEnsoleillement;
            })
            .map(ensoleillementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ensoleillement> findAll(Pageable pageable) {
        log.debug("Request to get all Ensoleillements");
        return ensoleillementRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ensoleillement> findOne(Long id) {
        log.debug("Request to get Ensoleillement : {}", id);
        return ensoleillementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ensoleillement : {}", id);
        ensoleillementRepository.deleteById(id);
    }
}
