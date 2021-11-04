package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.Mois;
import fr.syncrase.perma.repository.MoisRepository;
import fr.syncrase.perma.service.MoisService;
import fr.syncrase.perma.service.dto.MoisDTO;
import fr.syncrase.perma.service.mapper.MoisMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mois}.
 */
@Service
@Transactional
public class MoisServiceImpl implements MoisService {

    private final Logger log = LoggerFactory.getLogger(MoisServiceImpl.class);

    private final MoisRepository moisRepository;

    private final MoisMapper moisMapper;

    public MoisServiceImpl(MoisRepository moisRepository, MoisMapper moisMapper) {
        this.moisRepository = moisRepository;
        this.moisMapper = moisMapper;
    }

    @Override
    public MoisDTO save(MoisDTO moisDTO) {
        log.debug("Request to save Mois : {}", moisDTO);
        Mois mois = moisMapper.toEntity(moisDTO);
        mois = moisRepository.save(mois);
        return moisMapper.toDto(mois);
    }

    @Override
    public Optional<MoisDTO> partialUpdate(MoisDTO moisDTO) {
        log.debug("Request to partially update Mois : {}", moisDTO);

        return moisRepository
            .findById(moisDTO.getId())
            .map(existingMois -> {
                moisMapper.partialUpdate(existingMois, moisDTO);

                return existingMois;
            })
            .map(moisRepository::save)
            .map(moisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mois");
        return moisRepository.findAll(pageable).map(moisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoisDTO> findOne(Long id) {
        log.debug("Request to get Mois : {}", id);
        return moisRepository.findById(id).map(moisMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mois : {}", id);
        moisRepository.deleteById(id);
    }
}
