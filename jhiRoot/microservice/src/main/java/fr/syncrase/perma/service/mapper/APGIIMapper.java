package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.APGII;
import fr.syncrase.perma.service.dto.APGIIDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link APGII} and its DTO {@link APGIIDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface APGIIMapper extends EntityMapper<APGIIDTO, APGII> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    APGIIDTO toDtoId(APGII aPGII);
}
