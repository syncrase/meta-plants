package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Mois;
import fr.syncrase.perma.service.dto.MoisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mois} and its DTO {@link MoisDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MoisMapper extends EntityMapper<MoisDTO, Mois> {
    @Named("nom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    MoisDTO toDtoNom(Mois mois);
}
