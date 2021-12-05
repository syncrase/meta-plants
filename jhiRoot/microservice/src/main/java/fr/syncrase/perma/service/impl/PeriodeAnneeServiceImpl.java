package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.PeriodeAnnee;
import fr.syncrase.perma.repository.PeriodeAnneeRepository;
import fr.syncrase.perma.service.PeriodeAnneeService;
import fr.syncrase.perma.service.dto.PeriodeAnneeDTO;
import fr.syncrase.perma.service.mapper.PeriodeAnneeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PeriodeAnnee}.
 */
@Service
@Transactional
public class PeriodeAnneeServiceImpl implements PeriodeAnneeService {

    private final Logger log = LoggerFactory.getLogger(PeriodeAnneeServiceImpl.class);

    private final PeriodeAnneeRepository periodeAnneeRepository;

    private final PeriodeAnneeMapper periodeAnneeMapper;

    public PeriodeAnneeServiceImpl(PeriodeAnneeRepository periodeAnneeRepository, PeriodeAnneeMapper periodeAnneeMapper) {
        this.periodeAnneeRepository = periodeAnneeRepository;
        this.periodeAnneeMapper = periodeAnneeMapper;
    }

    @Override
    public PeriodeAnneeDTO save(PeriodeAnneeDTO periodeAnneeDTO) {
        log.debug("Request to save PeriodeAnnee : {}", periodeAnneeDTO);
        PeriodeAnnee periodeAnnee = periodeAnneeMapper.toEntity(periodeAnneeDTO);
        periodeAnnee = periodeAnneeRepository.save(periodeAnnee);
        return periodeAnneeMapper.toDto(periodeAnnee);
    }

    @Override
    public Optional<PeriodeAnneeDTO> partialUpdate(PeriodeAnneeDTO periodeAnneeDTO) {
        log.debug("Request to partially update PeriodeAnnee : {}", periodeAnneeDTO);

        return periodeAnneeRepository
            .findById(periodeAnneeDTO.getId())
            .map(existingPeriodeAnnee -> {
                periodeAnneeMapper.partialUpdate(existingPeriodeAnnee, periodeAnneeDTO);

                return existingPeriodeAnnee;
            })
            .map(periodeAnneeRepository::save)
            .map(periodeAnneeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PeriodeAnneeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PeriodeAnnees");
        return periodeAnneeRepository.findAll(pageable).map(periodeAnneeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PeriodeAnneeDTO> findOne(Long id) {
        log.debug("Request to get PeriodeAnnee : {}", id);
        return periodeAnneeRepository.findById(id).map(periodeAnneeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PeriodeAnnee : {}", id);
        periodeAnneeRepository.deleteById(id);
    }
}
