package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.repository.NomVernaculaireRepository;
import fr.syncrase.ecosyst.service.NomVernaculaireService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NomVernaculaire}.
 */
@Service
@Transactional
public class NomVernaculaireServiceImpl implements NomVernaculaireService {

    private final Logger log = LoggerFactory.getLogger(NomVernaculaireServiceImpl.class);

    private final NomVernaculaireRepository nomVernaculaireRepository;

    public NomVernaculaireServiceImpl(NomVernaculaireRepository nomVernaculaireRepository) {
        this.nomVernaculaireRepository = nomVernaculaireRepository;
    }

    @Override
    public NomVernaculaire save(NomVernaculaire nomVernaculaire) {
        log.debug("Request to save NomVernaculaire : {}", nomVernaculaire);
        return nomVernaculaireRepository.save(nomVernaculaire);
    }

    @Override
    public Optional<NomVernaculaire> partialUpdate(NomVernaculaire nomVernaculaire) {
        log.debug("Request to partially update NomVernaculaire : {}", nomVernaculaire);

        return nomVernaculaireRepository
            .findById(nomVernaculaire.getId())
            .map(existingNomVernaculaire -> {
                if (nomVernaculaire.getNom() != null) {
                    existingNomVernaculaire.setNom(nomVernaculaire.getNom());
                }
                if (nomVernaculaire.getDescription() != null) {
                    existingNomVernaculaire.setDescription(nomVernaculaire.getDescription());
                }

                return existingNomVernaculaire;
            })
            .map(nomVernaculaireRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NomVernaculaire> findAll(Pageable pageable) {
        log.debug("Request to get all NomVernaculaires");
        return nomVernaculaireRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NomVernaculaire> findOne(Long id) {
        log.debug("Request to get NomVernaculaire : {}", id);
        return nomVernaculaireRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NomVernaculaire : {}", id);
        nomVernaculaireRepository.deleteById(id);
    }
}
