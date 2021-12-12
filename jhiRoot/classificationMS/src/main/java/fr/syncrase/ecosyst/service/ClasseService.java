package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.Classe;
import fr.syncrase.ecosyst.repository.ClasseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Classe}.
 */
@Service
@Transactional
public class ClasseService {

    private final Logger log = LoggerFactory.getLogger(ClasseService.class);

    private final ClasseRepository classeRepository;

    public ClasseService(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    /**
     * Save a classe.
     *
     * @param classe the entity to save.
     * @return the persisted entity.
     */
    public Classe save(Classe classe) {
        log.debug("Request to save Classe : {}", classe);
        return classeRepository.save(classe);
    }

    /**
     * Partially update a classe.
     *
     * @param classe the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Classe> partialUpdate(Classe classe) {
        log.debug("Request to partially update Classe : {}", classe);

        return classeRepository
            .findById(classe.getId())
            .map(existingClasse -> {
                if (classe.getNomFr() != null) {
                    existingClasse.setNomFr(classe.getNomFr());
                }
                if (classe.getNomLatin() != null) {
                    existingClasse.setNomLatin(classe.getNomLatin());
                }

                return existingClasse;
            })
            .map(classeRepository::save);
    }

    /**
     * Get all the classes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Classe> findAll(Pageable pageable) {
        log.debug("Request to get all Classes");
        return classeRepository.findAll(pageable);
    }

    /**
     * Get one classe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Classe> findOne(Long id) {
        log.debug("Request to get Classe : {}", id);
        return classeRepository.findById(id);
    }

    /**
     * Delete the classe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Classe : {}", id);
        classeRepository.deleteById(id);
    }
}
