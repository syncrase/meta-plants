package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.service.ClassificationService;
import fr.syncrase.perma.domain.Classification;
import fr.syncrase.perma.repository.ClassificationRepository;
import fr.syncrase.perma.service.dto.ClassificationDTO;
import fr.syncrase.perma.service.mapper.ClassificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Classification}.
 */
@Service
@Transactional
public class ClassificationServiceImpl implements ClassificationService {

    private final Logger log = LoggerFactory.getLogger(ClassificationServiceImpl.class);

    private final ClassificationRepository classificationRepository;

    private final ClassificationMapper classificationMapper;

    public ClassificationServiceImpl(ClassificationRepository classificationRepository, ClassificationMapper classificationMapper) {
        this.classificationRepository = classificationRepository;
        this.classificationMapper = classificationMapper;
    }

    @Override
    public ClassificationDTO save(ClassificationDTO classificationDTO) {
        log.debug("Request to save Classification : {}", classificationDTO);
        Classification classification = classificationMapper.toEntity(classificationDTO);
        classification = classificationRepository.save(classification);
        return classificationMapper.toDto(classification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Classifications");
        return classificationRepository.findAll(pageable)
            .map(classificationMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ClassificationDTO> findOne(Long id) {
        log.debug("Request to get Classification : {}", id);
        return classificationRepository.findById(id)
            .map(classificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Classification : {}", id);
        classificationRepository.deleteById(id);
    }
}
