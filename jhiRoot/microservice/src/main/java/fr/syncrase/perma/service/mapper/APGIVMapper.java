package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.APGIV;
import fr.syncrase.perma.service.dto.APGIVDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link APGIV} and its DTO {@link APGIVDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface APGIVMapper extends EntityMapper<APGIVDTO, APGIV> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    APGIVDTO toDtoId(APGIV aPGIV);
}
