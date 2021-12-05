package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.NomVernaculaire;
import fr.syncrase.perma.service.dto.NomVernaculaireDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NomVernaculaire} and its DTO {@link NomVernaculaireDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NomVernaculaireMapper extends EntityMapper<NomVernaculaireDTO, NomVernaculaire> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<NomVernaculaireDTO> toDtoIdSet(Set<NomVernaculaire> nomVernaculaire);
}
