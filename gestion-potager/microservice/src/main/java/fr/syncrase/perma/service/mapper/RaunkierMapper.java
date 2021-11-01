package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.RaunkierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Raunkier} and its DTO {@link RaunkierDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RaunkierMapper extends EntityMapper<RaunkierDTO, Raunkier> {



    default Raunkier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Raunkier raunkier = new Raunkier();
        raunkier.setId(id);
        return raunkier;
    }
}
