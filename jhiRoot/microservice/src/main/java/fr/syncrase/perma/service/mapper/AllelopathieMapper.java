package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Allelopathie;
import fr.syncrase.perma.service.dto.AllelopathieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Allelopathie} and its DTO {@link AllelopathieDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlanteMapper.class })
public interface AllelopathieMapper extends EntityMapper<AllelopathieDTO, Allelopathie> {
    @Mapping(target = "cible", source = "cible", qualifiedByName = "id")
    @Mapping(target = "origine", source = "origine", qualifiedByName = "id")
    @Mapping(target = "interaction", source = "interaction", qualifiedByName = "id")
    AllelopathieDTO toDto(Allelopathie s);
}
