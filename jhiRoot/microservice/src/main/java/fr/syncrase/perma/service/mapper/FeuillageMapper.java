package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Feuillage;
import fr.syncrase.perma.service.dto.FeuillageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feuillage} and its DTO {@link FeuillageDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FeuillageMapper extends EntityMapper<FeuillageDTO, Feuillage> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FeuillageDTO toDtoId(Feuillage feuillage);
}
