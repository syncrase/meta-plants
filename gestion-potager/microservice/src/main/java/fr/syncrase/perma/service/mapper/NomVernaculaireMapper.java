package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.NomVernaculaireDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link NomVernaculaire} and its DTO {@link NomVernaculaireDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NomVernaculaireMapper extends EntityMapper<NomVernaculaireDTO, NomVernaculaire> {


    @Mapping(target = "plantes", ignore = true)
    @Mapping(target = "removePlantes", ignore = true)
    NomVernaculaire toEntity(NomVernaculaireDTO nomVernaculaireDTO);

    default NomVernaculaire fromId(Long id) {
        if (id == null) {
            return null;
        }
        NomVernaculaire nomVernaculaire = new NomVernaculaire();
        nomVernaculaire.setId(id);
        return nomVernaculaire;
    }
}
