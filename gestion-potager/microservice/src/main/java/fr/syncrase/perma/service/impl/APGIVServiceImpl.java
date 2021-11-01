package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.service.APGIVService;
import fr.syncrase.perma.domain.APGIV;
import fr.syncrase.perma.repository.APGIVRepository;
import fr.syncrase.perma.service.dto.APGIVDTO;
import fr.syncrase.perma.service.mapper.APGIVMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link APGIV}.
 */
@Service
@Transactional
public class APGIVServiceImpl implements APGIVService {

    private final Logger log = LoggerFactory.getLogger(APGIVServiceImpl.class);

    private final APGIVRepository aPGIVRepository;

    private final APGIVMapper aPGIVMapper;

    public APGIVServiceImpl(APGIVRepository aPGIVRepository, APGIVMapper aPGIVMapper) {
        this.aPGIVRepository = aPGIVRepository;
        this.aPGIVMapper = aPGIVMapper;
    }

    @Override
    public APGIVDTO save(APGIVDTO aPGIVDTO) {
        log.debug("Request to save APGIV : {}", aPGIVDTO);
        APGIV aPGIV = aPGIVMapper.toEntity(aPGIVDTO);
        aPGIV = aPGIVRepository.save(aPGIV);
        return aPGIVMapper.toDto(aPGIV);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<APGIVDTO> findAll(Pageable pageable) {
        log.debug("Request to get all APGIVS");
        return aPGIVRepository.findAll(pageable)
            .map(aPGIVMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<APGIVDTO> findOne(Long id) {
        log.debug("Request to get APGIV : {}", id);
        return aPGIVRepository.findById(id)
            .map(aPGIVMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete APGIV : {}", id);
        aPGIVRepository.deleteById(id);
    }
}
