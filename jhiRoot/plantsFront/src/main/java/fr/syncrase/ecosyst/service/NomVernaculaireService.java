package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.repository.NomVernaculaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link NomVernaculaire}.
 */
@Service
@Transactional
public class NomVernaculaireService {

    private final Logger log = LoggerFactory.getLogger(NomVernaculaireService.class);

    private final NomVernaculaireRepository nomVernaculaireRepository;

    public NomVernaculaireService(NomVernaculaireRepository nomVernaculaireRepository) {
        this.nomVernaculaireRepository = nomVernaculaireRepository;
    }

    /**
     * Save a nomVernaculaire.
     *
     * @param nomVernaculaire the entity to save.
     * @return the persisted entity.
     */
    public Mono<NomVernaculaire> save(NomVernaculaire nomVernaculaire) {
        log.debug("Request to save NomVernaculaire : {}", nomVernaculaire);
        return nomVernaculaireRepository.save(nomVernaculaire);
    }

    /**
     * Partially update a nomVernaculaire.
     *
     * @param nomVernaculaire the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<NomVernaculaire> partialUpdate(NomVernaculaire nomVernaculaire) {
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
            .flatMap(nomVernaculaireRepository::save);
    }

    /**
     * Get all the nomVernaculaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<NomVernaculaire> findAll(Pageable pageable) {
        log.debug("Request to get all NomVernaculaires");
        return nomVernaculaireRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of nomVernaculaires available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return nomVernaculaireRepository.count();
    }

    /**
     * Get one nomVernaculaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<NomVernaculaire> findOne(Long id) {
        log.debug("Request to get NomVernaculaire : {}", id);
        return nomVernaculaireRepository.findById(id);
    }

    /**
     * Delete the nomVernaculaire by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete NomVernaculaire : {}", id);
        return nomVernaculaireRepository.deleteById(id);
    }
}
