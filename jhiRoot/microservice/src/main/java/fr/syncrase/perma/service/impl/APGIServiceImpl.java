package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.APGI;
import fr.syncrase.perma.repository.APGIRepository;
import fr.syncrase.perma.service.APGIService;
import fr.syncrase.perma.service.dto.APGIDTO;
import fr.syncrase.perma.service.mapper.APGIMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link APGI}.
 */
@Service
@Transactional
public class APGIServiceImpl implements APGIService {

    private final Logger log = LoggerFactory.getLogger(APGIServiceImpl.class);

    private final APGIRepository aPGIRepository;

    private final APGIMapper aPGIMapper;

    public APGIServiceImpl(APGIRepository aPGIRepository, APGIMapper aPGIMapper) {
        this.aPGIRepository = aPGIRepository;
        this.aPGIMapper = aPGIMapper;
    }

    @Override
    public APGIDTO save(APGIDTO aPGIDTO) {
        log.debug("Request to save APGI : {}", aPGIDTO);
        APGI aPGI = aPGIMapper.toEntity(aPGIDTO);
        aPGI = aPGIRepository.save(aPGI);
        return aPGIMapper.toDto(aPGI);
    }

    @Override
    public Optional<APGIDTO> partialUpdate(APGIDTO aPGIDTO) {
        log.debug("Request to partially update APGI : {}", aPGIDTO);

        return aPGIRepository
            .findById(aPGIDTO.getId())
            .map(existingAPGI -> {
                aPGIMapper.partialUpdate(existingAPGI, aPGIDTO);

                return existingAPGI;
            })
            .map(aPGIRepository::save)
            .map(aPGIMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<APGIDTO> findAll(Pageable pageable) {
        log.debug("Request to get all APGIS");
        return aPGIRepository.findAll(pageable).map(aPGIMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<APGIDTO> findOne(Long id) {
        log.debug("Request to get APGI : {}", id);
        return aPGIRepository.findById(id).map(aPGIMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete APGI : {}", id);
        aPGIRepository.deleteById(id);
    }
}
