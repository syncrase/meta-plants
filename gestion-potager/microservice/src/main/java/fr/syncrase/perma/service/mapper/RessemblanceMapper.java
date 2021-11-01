package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.RessemblanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ressemblance} and its DTO {@link RessemblanceDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlanteMapper.class})
public interface RessemblanceMapper extends EntityMapper<RessemblanceDTO, Ressemblance> {

    @Mapping(source = "confusion.id", target = "confusionId")
    RessemblanceDTO toDto(Ressemblance ressemblance);

    @Mapping(source = "confusionId", target = "confusion")
    Ressemblance toEntity(RessemblanceDTO ressemblanceDTO);

    default Ressemblance fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ressemblance ressemblance = new Ressemblance();
        ressemblance.setId(id);
        return ressemblance;
    }
}
