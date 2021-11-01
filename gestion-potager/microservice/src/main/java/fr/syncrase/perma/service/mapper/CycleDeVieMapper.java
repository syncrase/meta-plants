package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.CycleDeVieDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CycleDeVie} and its DTO {@link CycleDeVieDTO}.
 */
@Mapper(componentModel = "spring", uses = {SemisMapper.class, PeriodeAnneeMapper.class})
public interface CycleDeVieMapper extends EntityMapper<CycleDeVieDTO, CycleDeVie> {

    @Mapping(source = "semis.id", target = "semisId")
    @Mapping(source = "apparitionFeuilles.id", target = "apparitionFeuillesId")
    @Mapping(source = "floraison.id", target = "floraisonId")
    @Mapping(source = "recolte.id", target = "recolteId")
    @Mapping(source = "croissance.id", target = "croissanceId")
    @Mapping(source = "maturite.id", target = "maturiteId")
    @Mapping(source = "plantation.id", target = "plantationId")
    @Mapping(source = "rempotage.id", target = "rempotageId")
    CycleDeVieDTO toDto(CycleDeVie cycleDeVie);

    @Mapping(source = "semisId", target = "semis")
    @Mapping(source = "apparitionFeuillesId", target = "apparitionFeuilles")
    @Mapping(source = "floraisonId", target = "floraison")
    @Mapping(source = "recolteId", target = "recolte")
    @Mapping(source = "croissanceId", target = "croissance")
    @Mapping(source = "maturiteId", target = "maturite")
    @Mapping(source = "plantationId", target = "plantation")
    @Mapping(source = "rempotageId", target = "rempotage")
    CycleDeVie toEntity(CycleDeVieDTO cycleDeVieDTO);

    default CycleDeVie fromId(Long id) {
        if (id == null) {
            return null;
        }
        CycleDeVie cycleDeVie = new CycleDeVie();
        cycleDeVie.setId(id);
        return cycleDeVie;
    }
}
