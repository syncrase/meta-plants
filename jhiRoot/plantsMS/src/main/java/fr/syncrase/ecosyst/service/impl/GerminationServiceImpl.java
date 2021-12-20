package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Germination;
import fr.syncrase.ecosyst.repository.GerminationRepository;
import fr.syncrase.ecosyst.service.GerminationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Germination}.
 */
@Service
@Transactional
public class GerminationServiceImpl implements GerminationService {

    private final Logger log = LoggerFactory.getLogger(GerminationServiceImpl.class);

    private final GerminationRepository germinationRepository;

    public GerminationServiceImpl(GerminationRepository germinationRepository) {
        this.germinationRepository = germinationRepository;
    }

    @Override
    public Germination save(Germination germination) {
        log.debug("Request to save Germination : {}", germination);
        return germinationRepository.save(germination);
    }

    @Override
    public Optional<Germination> partialUpdate(Germination germination) {
        log.debug("Request to partially update Germination : {}", germination);

        return germinationRepository
            .findById(germination.getId())
            .map(existingGermination -> {
                if (germination.getTempsDeGermination() != null) {
                    existingGermination.setTempsDeGermination(germination.getTempsDeGermination());
                }
                if (germination.getConditionDeGermination() != null) {
                    existingGermination.setConditionDeGermination(germination.getConditionDeGermination());
                }

                return existingGermination;
            })
            .map(germinationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Germination> findAll(Pageable pageable) {
        log.debug("Request to get all Germinations");
        return germinationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Germination> findOne(Long id) {
        log.debug("Request to get Germination : {}", id);
        return germinationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Germination : {}", id);
        germinationRepository.deleteById(id);
    }
}
