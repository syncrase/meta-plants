package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Reproduction;
import fr.syncrase.perma.service.dto.ReproductionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reproduction} and its DTO {@link ReproductionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReproductionMapper extends EntityMapper<ReproductionDTO, Reproduction> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReproductionDTO toDtoId(Reproduction reproduction);
}
