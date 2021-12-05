package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Allelopathie;
import fr.syncrase.perma.repository.AllelopathieRepository;
import fr.syncrase.perma.service.AllelopathieService;
import fr.syncrase.perma.service.dto.AllelopathieDTO;
import fr.syncrase.perma.service.mapper.AllelopathieMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Allelopathie}.
 */
@Service
@Transactional
public class AllelopathieServiceImpl implements AllelopathieService {

    private final Logger log = LoggerFactory.getLogger(AllelopathieServiceImpl.class);

    private final AllelopathieRepository allelopathieRepository;

    private final AllelopathieMapper allelopathieMapper;

    public AllelopathieServiceImpl(AllelopathieRepository allelopathieRepository, AllelopathieMapper allelopathieMapper) {
        this.allelopathieRepository = allelopathieRepository;
        this.allelopathieMapper = allelopathieMapper;
    }

    @Override
    public AllelopathieDTO save(AllelopathieDTO allelopathieDTO) {
        log.debug("Request to save Allelopathie : {}", allelopathieDTO);
        Allelopathie allelopathie = allelopathieMapper.toEntity(allelopathieDTO);
        allelopathie = allelopathieRepository.save(allelopathie);
        return allelopathieMapper.toDto(allelopathie);
    }

    @Override
    public Optional<AllelopathieDTO> partialUpdate(AllelopathieDTO allelopathieDTO) {
        log.debug("Request to partially update Allelopathie : {}", allelopathieDTO);

        return allelopathieRepository
            .findById(allelopathieDTO.getId())
            .map(existingAllelopathie -> {
                allelopathieMapper.partialUpdate(existingAllelopathie, allelopathieDTO);

                return existingAllelopathie;
            })
            .map(allelopathieRepository::save)
            .map(allelopathieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AllelopathieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Allelopathies");
        return allelopathieRepository.findAll(pageable).map(allelopathieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AllelopathieDTO> findOne(Long id) {
        log.debug("Request to get Allelopathie : {}", id);
        return allelopathieRepository.findById(id).map(allelopathieMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Allelopathie : {}", id);
        allelopathieRepository.deleteById(id);
    }
}
