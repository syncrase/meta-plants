package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.CronquistDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cronquist} and its DTO {@link CronquistDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CronquistMapper extends EntityMapper<CronquistDTO, Cronquist> {



    default Cronquist fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cronquist cronquist = new Cronquist();
        cronquist.setId(id);
        return cronquist;
    }
}
