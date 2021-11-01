package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.SolDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sol} and its DTO {@link SolDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SolMapper extends EntityMapper<SolDTO, Sol> {



    default Sol fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sol sol = new Sol();
        sol.setId(id);
        return sol;
    }
}
