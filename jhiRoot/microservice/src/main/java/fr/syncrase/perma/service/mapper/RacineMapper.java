package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Racine;
import fr.syncrase.perma.service.dto.RacineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Racine} and its DTO {@link RacineDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RacineMapper extends EntityMapper<RacineDTO, Racine> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RacineDTO toDtoId(Racine racine);
}
