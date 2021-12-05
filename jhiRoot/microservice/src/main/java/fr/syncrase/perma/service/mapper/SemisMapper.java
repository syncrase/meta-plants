package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Semis;
import fr.syncrase.perma.service.dto.SemisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Semis} and its DTO {@link SemisDTO}.
 */
@Mapper(componentModel = "spring", uses = { PeriodeAnneeMapper.class, TypeSemisMapper.class, GerminationMapper.class })
public interface SemisMapper extends EntityMapper<SemisDTO, Semis> {
    @Mapping(target = "semisPleineTerre", source = "semisPleineTerre", qualifiedByName = "id")
    @Mapping(target = "semisSousAbris", source = "semisSousAbris", qualifiedByName = "id")
    @Mapping(target = "typeSemis", source = "typeSemis", qualifiedByName = "id")
    @Mapping(target = "germination", source = "germination", qualifiedByName = "id")
    SemisDTO toDto(Semis s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SemisDTO toDtoId(Semis semis);
}
