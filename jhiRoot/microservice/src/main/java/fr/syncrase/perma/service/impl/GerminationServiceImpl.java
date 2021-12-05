package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Germination;
import fr.syncrase.perma.repository.GerminationRepository;
import fr.syncrase.perma.service.GerminationService;
import fr.syncrase.perma.service.dto.GerminationDTO;
import fr.syncrase.perma.service.mapper.GerminationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Germination}.
 */
@Service
@Transactional
public class GerminationServiceImpl implements GerminationService {

    private final Logger log = LoggerFactory.getLogger(GerminationServiceImpl.class);

    private final GerminationRepository germinationRepository;

    private final GerminationMapper germinationMapper;

    public GerminationServiceImpl(GerminationRepository germinationRepository, GerminationMapper germinationMapper) {
        this.germinationRepository = germinationRepository;
        this.germinationMapper = germinationMapper;
    }

    @Override
    public GerminationDTO save(GerminationDTO germinationDTO) {
        log.debug("Request to save Germination : {}", germinationDTO);
        Germination germination = germinationMapper.toEntity(germinationDTO);
        germination = germinationRepository.save(germination);
        return germinationMapper.toDto(germination);
    }

    @Override
    public Optional<GerminationDTO> partialUpdate(GerminationDTO germinationDTO) {
        log.debug("Request to partially update Germination : {}", germinationDTO);

        return germinationRepository
            .findById(germinationDTO.getId())
            .map(existingGermination -> {
                germinationMapper.partialUpdate(existingGermination, germinationDTO);

                return existingGermination;
            })
            .map(germinationRepository::save)
            .map(germinationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GerminationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Germinations");
        return germinationRepository.findAll(pageable).map(germinationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GerminationDTO> findOne(Long id) {
        log.debug("Request to get Germination : {}", id);
        return germinationRepository.findById(id).map(germinationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Germination : {}", id);
        germinationRepository.deleteById(id);
    }
}
