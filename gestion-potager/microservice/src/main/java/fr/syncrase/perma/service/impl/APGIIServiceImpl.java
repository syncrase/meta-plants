package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.APGII;
import fr.syncrase.perma.repository.APGIIRepository;
import fr.syncrase.perma.service.APGIIService;
import fr.syncrase.perma.service.dto.APGIIDTO;
import fr.syncrase.perma.service.mapper.APGIIMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link APGII}.
 */
@Service
@Transactional
public class APGIIServiceImpl implements APGIIService {

    private final Logger log = LoggerFactory.getLogger(APGIIServiceImpl.class);

    private final APGIIRepository aPGIIRepository;

    private final APGIIMapper aPGIIMapper;

    public APGIIServiceImpl(APGIIRepository aPGIIRepository, APGIIMapper aPGIIMapper) {
        this.aPGIIRepository = aPGIIRepository;
        this.aPGIIMapper = aPGIIMapper;
    }

    @Override
    public APGIIDTO save(APGIIDTO aPGIIDTO) {
        log.debug("Request to save APGII : {}", aPGIIDTO);
        APGII aPGII = aPGIIMapper.toEntity(aPGIIDTO);
        aPGII = aPGIIRepository.save(aPGII);
        return aPGIIMapper.toDto(aPGII);
    }

    @Override
    public Optional<APGIIDTO> partialUpdate(APGIIDTO aPGIIDTO) {
        log.debug("Request to partially update APGII : {}", aPGIIDTO);

        return aPGIIRepository
            .findById(aPGIIDTO.getId())
            .map(existingAPGII -> {
                aPGIIMapper.partialUpdate(existingAPGII, aPGIIDTO);

                return existingAPGII;
            })
            .map(aPGIIRepository::save)
            .map(aPGIIMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<APGIIDTO> findAll(Pageable pageable) {
        log.debug("Request to get all APGIIS");
        return aPGIIRepository.findAll(pageable).map(aPGIIMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<APGIIDTO> findOne(Long id) {
        log.debug("Request to get APGII : {}", id);
        return aPGIIRepository.findById(id).map(aPGIIMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete APGII : {}", id);
        aPGIIRepository.deleteById(id);
    }
}
