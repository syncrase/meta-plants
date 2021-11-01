package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.service.TypeSemisService;
import fr.syncrase.perma.domain.TypeSemis;
import fr.syncrase.perma.repository.TypeSemisRepository;
import fr.syncrase.perma.service.dto.TypeSemisDTO;
import fr.syncrase.perma.service.mapper.TypeSemisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TypeSemis}.
 */
@Service
@Transactional
public class TypeSemisServiceImpl implements TypeSemisService {

    private final Logger log = LoggerFactory.getLogger(TypeSemisServiceImpl.class);

    private final TypeSemisRepository typeSemisRepository;

    private final TypeSemisMapper typeSemisMapper;

    public TypeSemisServiceImpl(TypeSemisRepository typeSemisRepository, TypeSemisMapper typeSemisMapper) {
        this.typeSemisRepository = typeSemisRepository;
        this.typeSemisMapper = typeSemisMapper;
    }

    @Override
    public TypeSemisDTO save(TypeSemisDTO typeSemisDTO) {
        log.debug("Request to save TypeSemis : {}", typeSemisDTO);
        TypeSemis typeSemis = typeSemisMapper.toEntity(typeSemisDTO);
        typeSemis = typeSemisRepository.save(typeSemis);
        return typeSemisMapper.toDto(typeSemis);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeSemisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeSemis");
        return typeSemisRepository.findAll(pageable)
            .map(typeSemisMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TypeSemisDTO> findOne(Long id) {
        log.debug("Request to get TypeSemis : {}", id);
        return typeSemisRepository.findById(id)
            .map(typeSemisMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeSemis : {}", id);
        typeSemisRepository.deleteById(id);
    }
}
