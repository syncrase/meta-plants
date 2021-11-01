package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.MoisDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mois} and its DTO {@link MoisDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MoisMapper extends EntityMapper<MoisDTO, Mois> {



    default Mois fromId(Long id) {
        if (id == null) {
            return null;
        }
        Mois mois = new Mois();
        mois.setId(id);
        return mois;
    }
}
