package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.APGIII;
import fr.syncrase.perma.service.dto.APGIIIDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link APGIII} and its DTO {@link APGIIIDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface APGIIIMapper extends EntityMapper<APGIIIDTO, APGIII> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    APGIIIDTO toDtoId(APGIII aPGIII);
}
