package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Strate;
import fr.syncrase.ecosyst.repository.StrateRepository;
import fr.syncrase.ecosyst.service.StrateService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Strate}.
 */
@Service
@Transactional
public class StrateServiceImpl implements StrateService {

    private final Logger log = LoggerFactory.getLogger(StrateServiceImpl.class);

    private final StrateRepository strateRepository;

    public StrateServiceImpl(StrateRepository strateRepository) {
        this.strateRepository = strateRepository;
    }

    @Override
    public Strate save(Strate strate) {
        log.debug("Request to save Strate : {}", strate);
        return strateRepository.save(strate);
    }

    @Override
    public Optional<Strate> partialUpdate(Strate strate) {
        log.debug("Request to partially update Strate : {}", strate);

        return strateRepository
            .findById(strate.getId())
            .map(existingStrate -> {
                if (strate.getType() != null) {
                    existingStrate.setType(strate.getType());
                }

                return existingStrate;
            })
            .map(strateRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Strate> findAll(Pageable pageable) {
        log.debug("Request to get all Strates");
        return strateRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Strate> findOne(Long id) {
        log.debug("Request to get Strate : {}", id);
        return strateRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Strate : {}", id);
        strateRepository.deleteById(id);
    }
}
