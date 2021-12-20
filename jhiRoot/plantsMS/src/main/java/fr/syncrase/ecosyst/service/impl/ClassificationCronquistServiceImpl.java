package fr.syncrase.ecosyst.service.impl;

import fr.syncrase.ecosyst.domain.ClassificationCronquist;
import fr.syncrase.ecosyst.repository.ClassificationCronquistRepository;
import fr.syncrase.ecosyst.service.ClassificationCronquistService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClassificationCronquist}.
 */
@Service
@Transactional
public class ClassificationCronquistServiceImpl implements ClassificationCronquistService {

    private final Logger log = LoggerFactory.getLogger(ClassificationCronquistServiceImpl.class);

    private final ClassificationCronquistRepository classificationCronquistRepository;

    public ClassificationCronquistServiceImpl(ClassificationCronquistRepository classificationCronquistRepository) {
        this.classificationCronquistRepository = classificationCronquistRepository;
    }

    @Override
    public ClassificationCronquist save(ClassificationCronquist classificationCronquist) {
        log.debug("Request to save ClassificationCronquist : {}", classificationCronquist);
        return classificationCronquistRepository.save(classificationCronquist);
    }

    @Override
    public Optional<ClassificationCronquist> partialUpdate(ClassificationCronquist classificationCronquist) {
        log.debug("Request to partially update ClassificationCronquist : {}", classificationCronquist);

        return classificationCronquistRepository
            .findById(classificationCronquist.getId())
            .map(existingClassificationCronquist -> {
                if (classificationCronquist.getSuperRegne() != null) {
                    existingClassificationCronquist.setSuperRegne(classificationCronquist.getSuperRegne());
                }
                if (classificationCronquist.getRegne() != null) {
                    existingClassificationCronquist.setRegne(classificationCronquist.getRegne());
                }
                if (classificationCronquist.getSousRegne() != null) {
                    existingClassificationCronquist.setSousRegne(classificationCronquist.getSousRegne());
                }
                if (classificationCronquist.getRameau() != null) {
                    existingClassificationCronquist.setRameau(classificationCronquist.getRameau());
                }
                if (classificationCronquist.getInfraRegne() != null) {
                    existingClassificationCronquist.setInfraRegne(classificationCronquist.getInfraRegne());
                }
                if (classificationCronquist.getSuperEmbranchement() != null) {
                    existingClassificationCronquist.setSuperEmbranchement(classificationCronquist.getSuperEmbranchement());
                }
                if (classificationCronquist.getDivision() != null) {
                    existingClassificationCronquist.setDivision(classificationCronquist.getDivision());
                }
                if (classificationCronquist.getSousEmbranchement() != null) {
                    existingClassificationCronquist.setSousEmbranchement(classificationCronquist.getSousEmbranchement());
                }
                if (classificationCronquist.getInfraEmbranchement() != null) {
                    existingClassificationCronquist.setInfraEmbranchement(classificationCronquist.getInfraEmbranchement());
                }
                if (classificationCronquist.getMicroEmbranchement() != null) {
                    existingClassificationCronquist.setMicroEmbranchement(classificationCronquist.getMicroEmbranchement());
                }
                if (classificationCronquist.getSuperClasse() != null) {
                    existingClassificationCronquist.setSuperClasse(classificationCronquist.getSuperClasse());
                }
                if (classificationCronquist.getClasse() != null) {
                    existingClassificationCronquist.setClasse(classificationCronquist.getClasse());
                }
                if (classificationCronquist.getSousClasse() != null) {
                    existingClassificationCronquist.setSousClasse(classificationCronquist.getSousClasse());
                }
                if (classificationCronquist.getInfraClasse() != null) {
                    existingClassificationCronquist.setInfraClasse(classificationCronquist.getInfraClasse());
                }
                if (classificationCronquist.getSuperOrdre() != null) {
                    existingClassificationCronquist.setSuperOrdre(classificationCronquist.getSuperOrdre());
                }
                if (classificationCronquist.getOrdre() != null) {
                    existingClassificationCronquist.setOrdre(classificationCronquist.getOrdre());
                }
                if (classificationCronquist.getSousOrdre() != null) {
                    existingClassificationCronquist.setSousOrdre(classificationCronquist.getSousOrdre());
                }
                if (classificationCronquist.getInfraOrdre() != null) {
                    existingClassificationCronquist.setInfraOrdre(classificationCronquist.getInfraOrdre());
                }
                if (classificationCronquist.getMicroOrdre() != null) {
                    existingClassificationCronquist.setMicroOrdre(classificationCronquist.getMicroOrdre());
                }
                if (classificationCronquist.getSuperFamille() != null) {
                    existingClassificationCronquist.setSuperFamille(classificationCronquist.getSuperFamille());
                }
                if (classificationCronquist.getFamille() != null) {
                    existingClassificationCronquist.setFamille(classificationCronquist.getFamille());
                }
                if (classificationCronquist.getSousFamille() != null) {
                    existingClassificationCronquist.setSousFamille(classificationCronquist.getSousFamille());
                }
                if (classificationCronquist.getTribu() != null) {
                    existingClassificationCronquist.setTribu(classificationCronquist.getTribu());
                }
                if (classificationCronquist.getSousTribu() != null) {
                    existingClassificationCronquist.setSousTribu(classificationCronquist.getSousTribu());
                }
                if (classificationCronquist.getGenre() != null) {
                    existingClassificationCronquist.setGenre(classificationCronquist.getGenre());
                }
                if (classificationCronquist.getSousGenre() != null) {
                    existingClassificationCronquist.setSousGenre(classificationCronquist.getSousGenre());
                }
                if (classificationCronquist.getSection() != null) {
                    existingClassificationCronquist.setSection(classificationCronquist.getSection());
                }
                if (classificationCronquist.getSousSection() != null) {
                    existingClassificationCronquist.setSousSection(classificationCronquist.getSousSection());
                }
                if (classificationCronquist.getEspece() != null) {
                    existingClassificationCronquist.setEspece(classificationCronquist.getEspece());
                }
                if (classificationCronquist.getSousEspece() != null) {
                    existingClassificationCronquist.setSousEspece(classificationCronquist.getSousEspece());
                }
                if (classificationCronquist.getVariete() != null) {
                    existingClassificationCronquist.setVariete(classificationCronquist.getVariete());
                }
                if (classificationCronquist.getSousVariete() != null) {
                    existingClassificationCronquist.setSousVariete(classificationCronquist.getSousVariete());
                }
                if (classificationCronquist.getForme() != null) {
                    existingClassificationCronquist.setForme(classificationCronquist.getForme());
                }

                return existingClassificationCronquist;
            })
            .map(classificationCronquistRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassificationCronquist> findAll(Pageable pageable) {
        log.debug("Request to get all ClassificationCronquists");
        return classificationCronquistRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClassificationCronquist> findOne(Long id) {
        log.debug("Request to get ClassificationCronquist : {}", id);
        return classificationCronquistRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassificationCronquist : {}", id);
        classificationCronquistRepository.deleteById(id);
    }
}
