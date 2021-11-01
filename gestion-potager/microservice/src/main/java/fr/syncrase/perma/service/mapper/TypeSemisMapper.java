package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.TypeSemisDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeSemis} and its DTO {@link TypeSemisDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeSemisMapper extends EntityMapper<TypeSemisDTO, TypeSemis> {



    default TypeSemis fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeSemis typeSemis = new TypeSemis();
        typeSemis.setId(id);
        return typeSemis;
    }
}
