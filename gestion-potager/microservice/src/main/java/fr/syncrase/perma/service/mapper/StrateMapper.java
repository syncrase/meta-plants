package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Strate;
import fr.syncrase.perma.service.dto.StrateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Strate} and its DTO {@link StrateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StrateMapper extends EntityMapper<StrateDTO, Strate> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StrateDTO toDtoId(Strate strate);
}
