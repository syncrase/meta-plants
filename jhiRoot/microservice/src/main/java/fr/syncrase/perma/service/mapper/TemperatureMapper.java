package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Temperature;
import fr.syncrase.perma.service.dto.TemperatureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Temperature} and its DTO {@link TemperatureDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TemperatureMapper extends EntityMapper<TemperatureDTO, Temperature> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TemperatureDTO toDtoId(Temperature temperature);
}
