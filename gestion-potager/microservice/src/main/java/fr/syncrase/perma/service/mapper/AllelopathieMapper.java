package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.AllelopathieDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Allelopathie} and its DTO {@link AllelopathieDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlanteMapper.class})
public interface AllelopathieMapper extends EntityMapper<AllelopathieDTO, Allelopathie> {

    @Mapping(source = "cible.id", target = "cibleId")
    @Mapping(source = "origine.id", target = "origineId")
    @Mapping(source = "plante.id", target = "planteId")
    AllelopathieDTO toDto(Allelopathie allelopathie);

    @Mapping(source = "cibleId", target = "cible")
    @Mapping(source = "origineId", target = "origine")
    @Mapping(source = "planteId", target = "plante")
    Allelopathie toEntity(AllelopathieDTO allelopathieDTO);

    default Allelopathie fromId(Long id) {
        if (id == null) {
            return null;
        }
        Allelopathie allelopathie = new Allelopathie();
        allelopathie.setId(id);
        return allelopathie;
    }
}
