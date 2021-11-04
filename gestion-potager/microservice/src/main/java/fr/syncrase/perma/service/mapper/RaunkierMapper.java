package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Raunkier;
import fr.syncrase.perma.service.dto.RaunkierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Raunkier} and its DTO {@link RaunkierDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RaunkierMapper extends EntityMapper<RaunkierDTO, Raunkier> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RaunkierDTO toDtoId(Raunkier raunkier);
}
