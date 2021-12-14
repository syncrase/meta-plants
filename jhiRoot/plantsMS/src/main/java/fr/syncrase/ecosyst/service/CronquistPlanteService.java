package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.CronquistPlante;
import fr.syncrase.ecosyst.repository.CronquistPlanteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CronquistPlante}.
 */
@Service
@Transactional
public class CronquistPlanteService {

    private final Logger log = LoggerFactory.getLogger(CronquistPlanteService.class);

    private final CronquistPlanteRepository cronquistPlanteRepository;

    public CronquistPlanteService(CronquistPlanteRepository cronquistPlanteRepository) {
        this.cronquistPlanteRepository = cronquistPlanteRepository;
    }

    /**
     * Save a cronquistPlante.
     *
     * @param cronquistPlante the entity to save.
     * @return the persisted entity.
     */
    public CronquistPlante save(CronquistPlante cronquistPlante) {
        log.debug("Request to save CronquistPlante : {}", cronquistPlante);
        return cronquistPlanteRepository.save(cronquistPlante);
    }

    /**
     * Partially update a cronquistPlante.
     *
     * @param cronquistPlante the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CronquistPlante> partialUpdate(CronquistPlante cronquistPlante) {
        log.debug("Request to partially update CronquistPlante : {}", cronquistPlante);

        return cronquistPlanteRepository
            .findById(cronquistPlante.getId())
            .map(existingCronquistPlante -> {
                if (cronquistPlante.getSuperRegne() != null) {
                    existingCronquistPlante.setSuperRegne(cronquistPlante.getSuperRegne());
                }
                if (cronquistPlante.getRegne() != null) {
                    existingCronquistPlante.setRegne(cronquistPlante.getRegne());
                }
                if (cronquistPlante.getSousRegne() != null) {
                    existingCronquistPlante.setSousRegne(cronquistPlante.getSousRegne());
                }
                if (cronquistPlante.getRameau() != null) {
                    existingCronquistPlante.setRameau(cronquistPlante.getRameau());
                }
                if (cronquistPlante.getInfraRegne() != null) {
                    existingCronquistPlante.setInfraRegne(cronquistPlante.getInfraRegne());
                }
                if (cronquistPlante.getSuperDivision() != null) {
                    existingCronquistPlante.setSuperDivision(cronquistPlante.getSuperDivision());
                }
                if (cronquistPlante.getDivision() != null) {
                    existingCronquistPlante.setDivision(cronquistPlante.getDivision());
                }
                if (cronquistPlante.getSousDivision() != null) {
                    existingCronquistPlante.setSousDivision(cronquistPlante.getSousDivision());
                }
                if (cronquistPlante.getInfraEmbranchement() != null) {
                    existingCronquistPlante.setInfraEmbranchement(cronquistPlante.getInfraEmbranchement());
                }
                if (cronquistPlante.getMicroEmbranchement() != null) {
                    existingCronquistPlante.setMicroEmbranchement(cronquistPlante.getMicroEmbranchement());
                }
                if (cronquistPlante.getSuperClasse() != null) {
                    existingCronquistPlante.setSuperClasse(cronquistPlante.getSuperClasse());
                }
                if (cronquistPlante.getClasse() != null) {
                    existingCronquistPlante.setClasse(cronquistPlante.getClasse());
                }
                if (cronquistPlante.getSousClasse() != null) {
                    existingCronquistPlante.setSousClasse(cronquistPlante.getSousClasse());
                }
                if (cronquistPlante.getInfraClasse() != null) {
                    existingCronquistPlante.setInfraClasse(cronquistPlante.getInfraClasse());
                }
                if (cronquistPlante.getSuperOrdre() != null) {
                    existingCronquistPlante.setSuperOrdre(cronquistPlante.getSuperOrdre());
                }
                if (cronquistPlante.getOrdre() != null) {
                    existingCronquistPlante.setOrdre(cronquistPlante.getOrdre());
                }
                if (cronquistPlante.getSousOrdre() != null) {
                    existingCronquistPlante.setSousOrdre(cronquistPlante.getSousOrdre());
                }
                if (cronquistPlante.getInfraOrdre() != null) {
                    existingCronquistPlante.setInfraOrdre(cronquistPlante.getInfraOrdre());
                }
                if (cronquistPlante.getMicroOrdre() != null) {
                    existingCronquistPlante.setMicroOrdre(cronquistPlante.getMicroOrdre());
                }
                if (cronquistPlante.getSuperFamille() != null) {
                    existingCronquistPlante.setSuperFamille(cronquistPlante.getSuperFamille());
                }
                if (cronquistPlante.getFamille() != null) {
                    existingCronquistPlante.setFamille(cronquistPlante.getFamille());
                }
                if (cronquistPlante.getSousFamille() != null) {
                    existingCronquistPlante.setSousFamille(cronquistPlante.getSousFamille());
                }
                if (cronquistPlante.getTribu() != null) {
                    existingCronquistPlante.setTribu(cronquistPlante.getTribu());
                }
                if (cronquistPlante.getSousTribu() != null) {
                    existingCronquistPlante.setSousTribu(cronquistPlante.getSousTribu());
                }
                if (cronquistPlante.getGenre() != null) {
                    existingCronquistPlante.setGenre(cronquistPlante.getGenre());
                }
                if (cronquistPlante.getSousGenre() != null) {
                    existingCronquistPlante.setSousGenre(cronquistPlante.getSousGenre());
                }
                if (cronquistPlante.getSection() != null) {
                    existingCronquistPlante.setSection(cronquistPlante.getSection());
                }
                if (cronquistPlante.getSousSection() != null) {
                    existingCronquistPlante.setSousSection(cronquistPlante.getSousSection());
                }
                if (cronquistPlante.getEspece() != null) {
                    existingCronquistPlante.setEspece(cronquistPlante.getEspece());
                }
                if (cronquistPlante.getSousEspece() != null) {
                    existingCronquistPlante.setSousEspece(cronquistPlante.getSousEspece());
                }
                if (cronquistPlante.getVariete() != null) {
                    existingCronquistPlante.setVariete(cronquistPlante.getVariete());
                }
                if (cronquistPlante.getSousVariete() != null) {
                    existingCronquistPlante.setSousVariete(cronquistPlante.getSousVariete());
                }
                if (cronquistPlante.getForme() != null) {
                    existingCronquistPlante.setForme(cronquistPlante.getForme());
                }

                return existingCronquistPlante;
            })
            .map(cronquistPlanteRepository::save);
    }

    /**
     * Get all the cronquistPlantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CronquistPlante> findAll(Pageable pageable) {
        log.debug("Request to get all CronquistPlantes");
        return cronquistPlanteRepository.findAll(pageable);
    }

    /**
     * Get one cronquistPlante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CronquistPlante> findOne(Long id) {
        log.debug("Request to get CronquistPlante : {}", id);
        return cronquistPlanteRepository.findById(id);
    }

    /**
     * Delete the cronquistPlante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CronquistPlante : {}", id);
        cronquistPlanteRepository.deleteById(id);
    }
}
