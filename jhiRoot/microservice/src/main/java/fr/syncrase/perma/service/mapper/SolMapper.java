package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Sol;
import fr.syncrase.perma.service.dto.SolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sol} and its DTO {@link SolDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlanteMapper.class })
public interface SolMapper extends EntityMapper<SolDTO, Sol> {
    @Mapping(target = "plante", source = "plante", qualifiedByName = "id")
    SolDTO toDto(Sol s);
}
