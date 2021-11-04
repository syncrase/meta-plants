package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.APGIII;
import fr.syncrase.perma.repository.APGIIIRepository;
import fr.syncrase.perma.service.APGIIIService;
import fr.syncrase.perma.service.dto.APGIIIDTO;
import fr.syncrase.perma.service.mapper.APGIIIMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link APGIII}.
 */
@Service
@Transactional
public class APGIIIServiceImpl implements APGIIIService {

    private final Logger log = LoggerFactory.getLogger(APGIIIServiceImpl.class);

    private final APGIIIRepository aPGIIIRepository;

    private final APGIIIMapper aPGIIIMapper;

    public APGIIIServiceImpl(APGIIIRepository aPGIIIRepository, APGIIIMapper aPGIIIMapper) {
        this.aPGIIIRepository = aPGIIIRepository;
        this.aPGIIIMapper = aPGIIIMapper;
    }

    @Override
    public APGIIIDTO save(APGIIIDTO aPGIIIDTO) {
        log.debug("Request to save APGIII : {}", aPGIIIDTO);
        APGIII aPGIII = aPGIIIMapper.toEntity(aPGIIIDTO);
        aPGIII = aPGIIIRepository.save(aPGIII);
        return aPGIIIMapper.toDto(aPGIII);
    }

    @Override
    public Optional<APGIIIDTO> partialUpdate(APGIIIDTO aPGIIIDTO) {
        log.debug("Request to partially update APGIII : {}", aPGIIIDTO);

        return aPGIIIRepository
            .findById(aPGIIIDTO.getId())
            .map(existingAPGIII -> {
                aPGIIIMapper.partialUpdate(existingAPGIII, aPGIIIDTO);

                return existingAPGIII;
            })
            .map(aPGIIIRepository::save)
            .map(aPGIIIMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<APGIIIDTO> findAll(Pageable pageable) {
        log.debug("Request to get all APGIIIS");
        return aPGIIIRepository.findAll(pageable).map(aPGIIIMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<APGIIIDTO> findOne(Long id) {
        log.debug("Request to get APGIII : {}", id);
        return aPGIIIRepository.findById(id).map(aPGIIIMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete APGIII : {}", id);
        aPGIIIRepository.deleteById(id);
    }
}
