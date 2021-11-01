package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.APGIIIDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link APGIII} and its DTO {@link APGIIIDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface APGIIIMapper extends EntityMapper<APGIIIDTO, APGIII> {



    default APGIII fromId(Long id) {
        if (id == null) {
            return null;
        }
        APGIII aPGIII = new APGIII();
        aPGIII.setId(id);
        return aPGIII;
    }
}
