package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.APGI;
import fr.syncrase.perma.service.dto.APGIDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link APGI} and its DTO {@link APGIDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface APGIMapper extends EntityMapper<APGIDTO, APGI> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    APGIDTO toDtoId(APGI aPGI);
}
