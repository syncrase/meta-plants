package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.PlanteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plante} and its DTO {@link PlanteDTO}.
 */
@Mapper(componentModel = "spring", uses = {CycleDeVieMapper.class, ClassificationMapper.class, NomVernaculaireMapper.class})
public interface PlanteMapper extends EntityMapper<PlanteDTO, Plante> {

    @Mapping(source = "cycleDeVie.id", target = "cycleDeVieId")
    @Mapping(source = "classification.id", target = "classificationId")
    PlanteDTO toDto(Plante plante);

    @Mapping(source = "cycleDeVieId", target = "cycleDeVie")
    @Mapping(source = "classificationId", target = "classification")
    @Mapping(target = "confusions", ignore = true)
    @Mapping(target = "removeConfusions", ignore = true)
    @Mapping(target = "interactions", ignore = true)
    @Mapping(target = "removeInteractions", ignore = true)
    @Mapping(target = "removeNomsVernaculaires", ignore = true)
    @Mapping(target = "allelopathieRecue", ignore = true)
    @Mapping(target = "allelopathieProduite", ignore = true)
    Plante toEntity(PlanteDTO planteDTO);

    default Plante fromId(Long id) {
        if (id == null) {
            return null;
        }
        Plante plante = new Plante();
        plante.setId(id);
        return plante;
    }
}
