package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Cronquist;
import fr.syncrase.perma.service.dto.CronquistDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cronquist} and its DTO {@link CronquistDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CronquistMapper extends EntityMapper<CronquistDTO, Cronquist> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CronquistDTO toDtoId(Cronquist cronquist);
}
