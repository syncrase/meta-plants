package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.Mois;
import fr.syncrase.ecosyst.repository.MoisRepository;
import fr.syncrase.ecosyst.service.MoisService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mois}.
 */
@Service
@Transactional
public class MoisServiceImpl implements MoisService {

    private final Logger log = LoggerFactory.getLogger(MoisServiceImpl.class);

    private final MoisRepository moisRepository;

    public MoisServiceImpl(MoisRepository moisRepository) {
        this.moisRepository = moisRepository;
    }

    @Override
    public Mois save(Mois mois) {
        log.debug("Request to save Mois : {}", mois);
        return moisRepository.save(mois);
    }

    @Override
    public Optional<Mois> partialUpdate(Mois mois) {
        log.debug("Request to partially update Mois : {}", mois);

        return moisRepository
            .findById(mois.getId())
            .map(existingMois -> {
                if (mois.getNumero() != null) {
                    existingMois.setNumero(mois.getNumero());
                }
                if (mois.getNom() != null) {
                    existingMois.setNom(mois.getNom());
                }

                return existingMois;
            })
            .map(moisRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Mois> findAll(Pageable pageable) {
        log.debug("Request to get all Mois");
        return moisRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mois> findOne(Long id) {
        log.debug("Request to get Mois : {}", id);
        return moisRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mois : {}", id);
        moisRepository.deleteById(id);
    }
}
