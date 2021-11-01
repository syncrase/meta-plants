package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.APGIIDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link APGII} and its DTO {@link APGIIDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface APGIIMapper extends EntityMapper<APGIIDTO, APGII> {



    default APGII fromId(Long id) {
        if (id == null) {
            return null;
        }
        APGII aPGII = new APGII();
        aPGII.setId(id);
        return aPGII;
    }
}
