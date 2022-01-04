package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import fr.syncrase.ecosyst.service.ClassificationNomService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClassificationNom}.
 */
@Service
@Transactional
public class ClassificationNomServiceImpl implements ClassificationNomService {

    private final Logger log = LoggerFactory.getLogger(ClassificationNomServiceImpl.class);

    private final ClassificationNomRepository classificationNomRepository;

    public ClassificationNomServiceImpl(ClassificationNomRepository classificationNomRepository) {
        this.classificationNomRepository = classificationNomRepository;
    }

    @Override
    public ClassificationNom save(ClassificationNom classificationNom) {
        log.debug("Request to save ClassificationNom : {}", classificationNom);
        return classificationNomRepository.save(classificationNom);
    }

    @Override
    public Optional<ClassificationNom> partialUpdate(ClassificationNom classificationNom) {
        log.debug("Request to partially update ClassificationNom : {}", classificationNom);

        return classificationNomRepository
            .findById(classificationNom.getId())
            .map(existingClassificationNom -> {
                if (classificationNom.getNomFr() != null) {
                    existingClassificationNom.setNomFr(classificationNom.getNomFr());
                }
                if (classificationNom.getNomLatin() != null) {
                    existingClassificationNom.setNomLatin(classificationNom.getNomLatin());
                }

                return existingClassificationNom;
            })
            .map(classificationNomRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassificationNom> findAll(Pageable pageable) {
        log.debug("Request to get all ClassificationNoms");
        return classificationNomRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClassificationNom> findOne(Long id) {
        log.debug("Request to get ClassificationNom : {}", id);
        return classificationNomRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassificationNom : {}", id);
        classificationNomRepository.deleteById(id);
    }
}
