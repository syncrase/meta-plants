package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.APGIDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link APGI} and its DTO {@link APGIDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface APGIMapper extends EntityMapper<APGIDTO, APGI> {



    default APGI fromId(Long id) {
        if (id == null) {
            return null;
        }
        APGI aPGI = new APGI();
        aPGI.setId(id);
        return aPGI;
    }
}
