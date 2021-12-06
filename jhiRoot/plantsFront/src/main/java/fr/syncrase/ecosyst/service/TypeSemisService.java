package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.TypeSemis;
import fr.syncrase.ecosyst.repository.TypeSemisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TypeSemis}.
 */
@Service
@Transactional
public class TypeSemisService {

    private final Logger log = LoggerFactory.getLogger(TypeSemisService.class);

    private final TypeSemisRepository typeSemisRepository;

    public TypeSemisService(TypeSemisRepository typeSemisRepository) {
        this.typeSemisRepository = typeSemisRepository;
    }

    /**
     * Save a typeSemis.
     *
     * @param typeSemis the entity to save.
     * @return the persisted entity.
     */
    public Mono<TypeSemis> save(TypeSemis typeSemis) {
        log.debug("Request to save TypeSemis : {}", typeSemis);
        return typeSemisRepository.save(typeSemis);
    }

    /**
     * Partially update a typeSemis.
     *
     * @param typeSemis the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TypeSemis> partialUpdate(TypeSemis typeSemis) {
        log.debug("Request to partially update TypeSemis : {}", typeSemis);

        return typeSemisRepository
            .findById(typeSemis.getId())
            .map(existingTypeSemis -> {
                if (typeSemis.getType() != null) {
                    existingTypeSemis.setType(typeSemis.getType());
                }
                if (typeSemis.getDescription() != null) {
                    existingTypeSemis.setDescription(typeSemis.getDescription());
                }

                return existingTypeSemis;
            })
            .flatMap(typeSemisRepository::save);
    }

    /**
     * Get all the typeSemis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TypeSemis> findAll(Pageable pageable) {
        log.debug("Request to get all TypeSemis");
        return typeSemisRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of typeSemis available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return typeSemisRepository.count();
    }

    /**
     * Get one typeSemis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TypeSemis> findOne(Long id) {
        log.debug("Request to get TypeSemis : {}", id);
        return typeSemisRepository.findById(id);
    }

    /**
     * Delete the typeSemis by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TypeSemis : {}", id);
        return typeSemisRepository.deleteById(id);
    }
}
