package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Ensoleillement;
import fr.syncrase.perma.repository.EnsoleillementRepository;
import fr.syncrase.perma.service.EnsoleillementService;
import fr.syncrase.perma.service.dto.EnsoleillementDTO;
import fr.syncrase.perma.service.mapper.EnsoleillementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ensoleillement}.
 */
@Service
@Transactional
public class EnsoleillementServiceImpl implements EnsoleillementService {

    private final Logger log = LoggerFactory.getLogger(EnsoleillementServiceImpl.class);

    private final EnsoleillementRepository ensoleillementRepository;

    private final EnsoleillementMapper ensoleillementMapper;

    public EnsoleillementServiceImpl(EnsoleillementRepository ensoleillementRepository, EnsoleillementMapper ensoleillementMapper) {
        this.ensoleillementRepository = ensoleillementRepository;
        this.ensoleillementMapper = ensoleillementMapper;
    }

    @Override
    public EnsoleillementDTO save(EnsoleillementDTO ensoleillementDTO) {
        log.debug("Request to save Ensoleillement : {}", ensoleillementDTO);
        Ensoleillement ensoleillement = ensoleillementMapper.toEntity(ensoleillementDTO);
        ensoleillement = ensoleillementRepository.save(ensoleillement);
        return ensoleillementMapper.toDto(ensoleillement);
    }

    @Override
    public Optional<EnsoleillementDTO> partialUpdate(EnsoleillementDTO ensoleillementDTO) {
        log.debug("Request to partially update Ensoleillement : {}", ensoleillementDTO);

        return ensoleillementRepository
            .findById(ensoleillementDTO.getId())
            .map(existingEnsoleillement -> {
                ensoleillementMapper.partialUpdate(existingEnsoleillement, ensoleillementDTO);

                return existingEnsoleillement;
            })
            .map(ensoleillementRepository::save)
            .map(ensoleillementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnsoleillementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ensoleillements");
        return ensoleillementRepository.findAll(pageable).map(ensoleillementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnsoleillementDTO> findOne(Long id) {
        log.debug("Request to get Ensoleillement : {}", id);
        return ensoleillementRepository.findById(id).map(ensoleillementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ensoleillement : {}", id);
        ensoleillementRepository.deleteById(id);
    }
}
