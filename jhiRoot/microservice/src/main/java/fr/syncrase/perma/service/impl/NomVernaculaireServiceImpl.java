package fr.syncrase.perma.service.impl;

import fr.syncrase.perma.domain.NomVernaculaire;
import fr.syncrase.perma.repository.NomVernaculaireRepository;
import fr.syncrase.perma.service.NomVernaculaireService;
import fr.syncrase.perma.service.dto.NomVernaculaireDTO;
import fr.syncrase.perma.service.mapper.NomVernaculaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NomVernaculaire}.
 */
@Service
@Transactional
public class NomVernaculaireServiceImpl implements NomVernaculaireService {

    private final Logger log = LoggerFactory.getLogger(NomVernaculaireServiceImpl.class);

    private final NomVernaculaireRepository nomVernaculaireRepository;

    private final NomVernaculaireMapper nomVernaculaireMapper;

    public NomVernaculaireServiceImpl(NomVernaculaireRepository nomVernaculaireRepository, NomVernaculaireMapper nomVernaculaireMapper) {
        this.nomVernaculaireRepository = nomVernaculaireRepository;
        this.nomVernaculaireMapper = nomVernaculaireMapper;
    }

    @Override
    public NomVernaculaireDTO save(NomVernaculaireDTO nomVernaculaireDTO) {
        log.debug("Request to save NomVernaculaire : {}", nomVernaculaireDTO);
        NomVernaculaire nomVernaculaire = nomVernaculaireMapper.toEntity(nomVernaculaireDTO);
        nomVernaculaire = nomVernaculaireRepository.save(nomVernaculaire);
        return nomVernaculaireMapper.toDto(nomVernaculaire);
    }

    @Override
    public Optional<NomVernaculaireDTO> partialUpdate(NomVernaculaireDTO nomVernaculaireDTO) {
        log.debug("Request to partially update NomVernaculaire : {}", nomVernaculaireDTO);

        return nomVernaculaireRepository
            .findById(nomVernaculaireDTO.getId())
            .map(existingNomVernaculaire -> {
                nomVernaculaireMapper.partialUpdate(existingNomVernaculaire, nomVernaculaireDTO);

                return existingNomVernaculaire;
            })
            .map(nomVernaculaireRepository::save)
            .map(nomVernaculaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NomVernaculaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NomVernaculaires");
        return nomVernaculaireRepository.findAll(pageable).map(nomVernaculaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NomVernaculaireDTO> findOne(Long id) {
        log.debug("Request to get NomVernaculaire : {}", id);
        return nomVernaculaireRepository.findById(id).map(nomVernaculaireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NomVernaculaire : {}", id);
        nomVernaculaireRepository.deleteById(id);
    }
}
