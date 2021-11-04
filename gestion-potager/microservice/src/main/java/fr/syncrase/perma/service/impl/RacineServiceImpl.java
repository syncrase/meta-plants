package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Racine;
import fr.syncrase.perma.repository.RacineRepository;
import fr.syncrase.perma.service.RacineService;
import fr.syncrase.perma.service.dto.RacineDTO;
import fr.syncrase.perma.service.mapper.RacineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Racine}.
 */
@Service
@Transactional
public class RacineServiceImpl implements RacineService {

    private final Logger log = LoggerFactory.getLogger(RacineServiceImpl.class);

    private final RacineRepository racineRepository;

    private final RacineMapper racineMapper;

    public RacineServiceImpl(RacineRepository racineRepository, RacineMapper racineMapper) {
        this.racineRepository = racineRepository;
        this.racineMapper = racineMapper;
    }

    @Override
    public RacineDTO save(RacineDTO racineDTO) {
        log.debug("Request to save Racine : {}", racineDTO);
        Racine racine = racineMapper.toEntity(racineDTO);
        racine = racineRepository.save(racine);
        return racineMapper.toDto(racine);
    }

    @Override
    public Optional<RacineDTO> partialUpdate(RacineDTO racineDTO) {
        log.debug("Request to partially update Racine : {}", racineDTO);

        return racineRepository
            .findById(racineDTO.getId())
            .map(existingRacine -> {
                racineMapper.partialUpdate(existingRacine, racineDTO);

                return existingRacine;
            })
            .map(racineRepository::save)
            .map(racineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RacineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Racines");
        return racineRepository.findAll(pageable).map(racineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RacineDTO> findOne(Long id) {
        log.debug("Request to get Racine : {}", id);
        return racineRepository.findById(id).map(racineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Racine : {}", id);
        racineRepository.deleteById(id);
    }
}
