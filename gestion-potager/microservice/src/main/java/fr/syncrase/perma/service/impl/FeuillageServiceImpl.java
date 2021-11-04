package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Feuillage;
import fr.syncrase.perma.repository.FeuillageRepository;
import fr.syncrase.perma.service.FeuillageService;
import fr.syncrase.perma.service.dto.FeuillageDTO;
import fr.syncrase.perma.service.mapper.FeuillageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Feuillage}.
 */
@Service
@Transactional
public class FeuillageServiceImpl implements FeuillageService {

    private final Logger log = LoggerFactory.getLogger(FeuillageServiceImpl.class);

    private final FeuillageRepository feuillageRepository;

    private final FeuillageMapper feuillageMapper;

    public FeuillageServiceImpl(FeuillageRepository feuillageRepository, FeuillageMapper feuillageMapper) {
        this.feuillageRepository = feuillageRepository;
        this.feuillageMapper = feuillageMapper;
    }

    @Override
    public FeuillageDTO save(FeuillageDTO feuillageDTO) {
        log.debug("Request to save Feuillage : {}", feuillageDTO);
        Feuillage feuillage = feuillageMapper.toEntity(feuillageDTO);
        feuillage = feuillageRepository.save(feuillage);
        return feuillageMapper.toDto(feuillage);
    }

    @Override
    public Optional<FeuillageDTO> partialUpdate(FeuillageDTO feuillageDTO) {
        log.debug("Request to partially update Feuillage : {}", feuillageDTO);

        return feuillageRepository
            .findById(feuillageDTO.getId())
            .map(existingFeuillage -> {
                feuillageMapper.partialUpdate(existingFeuillage, feuillageDTO);

                return existingFeuillage;
            })
            .map(feuillageRepository::save)
            .map(feuillageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeuillageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Feuillages");
        return feuillageRepository.findAll(pageable).map(feuillageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeuillageDTO> findOne(Long id) {
        log.debug("Request to get Feuillage : {}", id);
        return feuillageRepository.findById(id).map(feuillageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Feuillage : {}", id);
        feuillageRepository.deleteById(id);
    }
}
