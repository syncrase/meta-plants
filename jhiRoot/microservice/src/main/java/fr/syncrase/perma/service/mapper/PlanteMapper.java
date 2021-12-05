package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.service.dto.PlanteDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plante} and its DTO {@link PlanteDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        CycleDeVieMapper.class,
        ClassificationMapper.class,
        NomVernaculaireMapper.class,
        TemperatureMapper.class,
        RacineMapper.class,
        StrateMapper.class,
        FeuillageMapper.class,
    }
)
public interface PlanteMapper extends EntityMapper<PlanteDTO, Plante> {
    @Mapping(target = "cycleDeVie", source = "cycleDeVie", qualifiedByName = "id")
    @Mapping(target = "classification", source = "classification", qualifiedByName = "id")
    @Mapping(target = "nomsVernaculaires", source = "nomsVernaculaires", qualifiedByName = "idSet")
    @Mapping(target = "temperature", source = "temperature", qualifiedByName = "id")
    @Mapping(target = "racine", source = "racine", qualifiedByName = "id")
    @Mapping(target = "strate", source = "strate", qualifiedByName = "id")
    @Mapping(target = "feuillage", source = "feuillage", qualifiedByName = "id")
    PlanteDTO toDto(Plante s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlanteDTO toDtoId(Plante plante);

    @Mapping(target = "removeNomsVernaculaires", ignore = true)
    Plante toEntity(PlanteDTO planteDTO);
}
