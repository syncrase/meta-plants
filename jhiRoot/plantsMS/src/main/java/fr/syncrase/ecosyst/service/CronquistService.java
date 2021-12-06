package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Cronquist;
import fr.syncrase.ecosyst.repository.CronquistRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cronquist}.
 */
@Service
@Transactional
public class CronquistService {

    private final Logger log = LoggerFactory.getLogger(CronquistService.class);

    private final CronquistRepository cronquistRepository;

    public CronquistService(CronquistRepository cronquistRepository) {
        this.cronquistRepository = cronquistRepository;
    }

    /**
     * Save a cronquist.
     *
     * @param cronquist the entity to save.
     * @return the persisted entity.
     */
    public Cronquist save(Cronquist cronquist) {
        log.debug("Request to save Cronquist : {}", cronquist);
        return cronquistRepository.save(cronquist);
    }

    /**
     * Partially update a cronquist.
     *
     * @param cronquist the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Cronquist> partialUpdate(Cronquist cronquist) {
        log.debug("Request to partially update Cronquist : {}", cronquist);

        return cronquistRepository
            .findById(cronquist.getId())
            .map(existingCronquist -> {
                if (cronquist.getRegne() != null) {
                    existingCronquist.setRegne(cronquist.getRegne());
                }
                if (cronquist.getSousRegne() != null) {
                    existingCronquist.setSousRegne(cronquist.getSousRegne());
                }
                if (cronquist.getDivision() != null) {
                    existingCronquist.setDivision(cronquist.getDivision());
                }
                if (cronquist.getClasse() != null) {
                    existingCronquist.setClasse(cronquist.getClasse());
                }
                if (cronquist.getSousClasse() != null) {
                    existingCronquist.setSousClasse(cronquist.getSousClasse());
                }
                if (cronquist.getOrdre() != null) {
                    existingCronquist.setOrdre(cronquist.getOrdre());
                }
                if (cronquist.getFamille() != null) {
                    existingCronquist.setFamille(cronquist.getFamille());
                }
                if (cronquist.getGenre() != null) {
                    existingCronquist.setGenre(cronquist.getGenre());
                }
                if (cronquist.getEspece() != null) {
                    existingCronquist.setEspece(cronquist.getEspece());
                }

                return existingCronquist;
            })
            .map(cronquistRepository::save);
    }

    /**
     * Get all the cronquists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Cronquist> findAll(Pageable pageable) {
        log.debug("Request to get all Cronquists");
        return cronquistRepository.findAll(pageable);
    }

    /**
     * Get one cronquist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Cronquist> findOne(Long id) {
        log.debug("Request to get Cronquist : {}", id);
        return cronquistRepository.findById(id);
    }

    /**
     * Delete the cronquist by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cronquist : {}", id);
        cronquistRepository.deleteById(id);
    }
}
