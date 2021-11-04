package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Ressemblance;
import fr.syncrase.perma.service.dto.RessemblanceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ressemblance} and its DTO {@link RessemblanceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlanteMapper.class })
public interface RessemblanceMapper extends EntityMapper<RessemblanceDTO, Ressemblance> {
    @Mapping(target = "confusion", source = "confusion", qualifiedByName = "id")
    RessemblanceDTO toDto(Ressemblance s);
}
