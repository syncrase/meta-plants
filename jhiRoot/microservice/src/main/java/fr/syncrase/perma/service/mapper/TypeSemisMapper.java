package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.TypeSemis;
import fr.syncrase.perma.service.dto.TypeSemisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeSemis} and its DTO {@link TypeSemisDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeSemisMapper extends EntityMapper<TypeSemisDTO, TypeSemis> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeSemisDTO toDtoId(TypeSemis typeSemis);
}
