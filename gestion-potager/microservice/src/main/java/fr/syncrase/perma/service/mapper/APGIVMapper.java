package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.APGIVDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link APGIV} and its DTO {@link APGIVDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface APGIVMapper extends EntityMapper<APGIVDTO, APGIV> {



    default APGIV fromId(Long id) {
        if (id == null) {
            return null;
        }
        APGIV aPGIV = new APGIV();
        aPGIV.setId(id);
        return aPGIV;
    }
}
