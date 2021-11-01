package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.SemisDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Semis} and its DTO {@link SemisDTO}.
 */
@Mapper(componentModel = "spring", uses = {PeriodeAnneeMapper.class, TypeSemisMapper.class, GerminationMapper.class})
public interface SemisMapper extends EntityMapper<SemisDTO, Semis> {

    @Mapping(source = "semisPleineTerre.id", target = "semisPleineTerreId")
    @Mapping(source = "semisSousAbris.id", target = "semisSousAbrisId")
    @Mapping(source = "typeSemis.id", target = "typeSemisId")
    @Mapping(source = "germination.id", target = "germinationId")
    SemisDTO toDto(Semis semis);

    @Mapping(source = "semisPleineTerreId", target = "semisPleineTerre")
    @Mapping(source = "semisSousAbrisId", target = "semisSousAbris")
    @Mapping(source = "typeSemisId", target = "typeSemis")
    @Mapping(source = "germinationId", target = "germination")
    Semis toEntity(SemisDTO semisDTO);

    default Semis fromId(Long id) {
        if (id == null) {
            return null;
        }
        Semis semis = new Semis();
        semis.setId(id);
        return semis;
    }
}
