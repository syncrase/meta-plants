package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.GerminationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Germination} and its DTO {@link GerminationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GerminationMapper extends EntityMapper<GerminationDTO, Germination> {



    default Germination fromId(Long id) {
        if (id == null) {
            return null;
        }
        Germination germination = new Germination();
        germination.setId(id);
        return germination;
    }
}
