package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Ensoleillement;
import fr.syncrase.perma.service.dto.EnsoleillementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ensoleillement} and its DTO {@link EnsoleillementDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlanteMapper.class })
public interface EnsoleillementMapper extends EntityMapper<EnsoleillementDTO, Ensoleillement> {
    @Mapping(target = "plante", source = "plante", qualifiedByName = "id")
    EnsoleillementDTO toDto(Ensoleillement s);
}
