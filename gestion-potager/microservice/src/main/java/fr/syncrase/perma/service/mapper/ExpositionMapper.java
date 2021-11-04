package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Exposition;
import fr.syncrase.perma.service.dto.ExpositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exposition} and its DTO {@link ExpositionDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlanteMapper.class })
public interface ExpositionMapper extends EntityMapper<ExpositionDTO, Exposition> {
    @Mapping(target = "plante", source = "plante", qualifiedByName = "id")
    ExpositionDTO toDto(Exposition s);
}
