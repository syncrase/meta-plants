package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Germination;
import fr.syncrase.perma.service.dto.GerminationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Germination} and its DTO {@link GerminationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GerminationMapper extends EntityMapper<GerminationDTO, Germination> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GerminationDTO toDtoId(Germination germination);
}
