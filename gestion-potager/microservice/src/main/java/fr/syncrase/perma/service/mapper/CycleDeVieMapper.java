package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.CycleDeVie;
import fr.syncrase.perma.service.dto.CycleDeVieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CycleDeVie} and its DTO {@link CycleDeVieDTO}.
 */
@Mapper(componentModel = "spring", uses = { SemisMapper.class, PeriodeAnneeMapper.class, ReproductionMapper.class })
public interface CycleDeVieMapper extends EntityMapper<CycleDeVieDTO, CycleDeVie> {
    @Mapping(target = "semis", source = "semis", qualifiedByName = "id")
    @Mapping(target = "apparitionFeuilles", source = "apparitionFeuilles", qualifiedByName = "id")
    @Mapping(target = "floraison", source = "floraison", qualifiedByName = "id")
    @Mapping(target = "recolte", source = "recolte", qualifiedByName = "id")
    @Mapping(target = "croissance", source = "croissance", qualifiedByName = "id")
    @Mapping(target = "maturite", source = "maturite", qualifiedByName = "id")
    @Mapping(target = "plantation", source = "plantation", qualifiedByName = "id")
    @Mapping(target = "rempotage", source = "rempotage", qualifiedByName = "id")
    @Mapping(target = "reproduction", source = "reproduction", qualifiedByName = "id")
    CycleDeVieDTO toDto(CycleDeVie s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CycleDeVieDTO toDtoId(CycleDeVie cycleDeVie);
}
