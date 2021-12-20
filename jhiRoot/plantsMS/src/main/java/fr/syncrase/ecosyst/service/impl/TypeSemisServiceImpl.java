package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.TypeSemis;
import fr.syncrase.ecosyst.repository.TypeSemisRepository;
import fr.syncrase.ecosyst.service.TypeSemisService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeSemis}.
 */
@Service
@Transactional
public class TypeSemisServiceImpl implements TypeSemisService {

    private final Logger log = LoggerFactory.getLogger(TypeSemisServiceImpl.class);

    private final TypeSemisRepository typeSemisRepository;

    public TypeSemisServiceImpl(TypeSemisRepository typeSemisRepository) {
        this.typeSemisRepository = typeSemisRepository;
    }

    @Override
    public TypeSemis save(TypeSemis typeSemis) {
        log.debug("Request to save TypeSemis : {}", typeSemis);
        return typeSemisRepository.save(typeSemis);
    }

    @Override
    public Optional<TypeSemis> partialUpdate(TypeSemis typeSemis) {
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
            .map(typeSemisRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeSemis> findAll(Pageable pageable) {
        log.debug("Request to get all TypeSemis");
        return typeSemisRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeSemis> findOne(Long id) {
        log.debug("Request to get TypeSemis : {}", id);
        return typeSemisRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeSemis : {}", id);
        typeSemisRepository.deleteById(id);
    }
}
