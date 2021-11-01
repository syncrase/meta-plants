package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.PeriodeAnneeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PeriodeAnnee} and its DTO {@link PeriodeAnneeDTO}.
 */
@Mapper(componentModel = "spring", uses = {MoisMapper.class})
public interface PeriodeAnneeMapper extends EntityMapper<PeriodeAnneeDTO, PeriodeAnnee> {

    @Mapping(source = "debut.id", target = "debutId")
    @Mapping(source = "debut.nom", target = "debutNom")
    @Mapping(source = "fin.id", target = "finId")
    @Mapping(source = "fin.nom", target = "finNom")
    PeriodeAnneeDTO toDto(PeriodeAnnee periodeAnnee);

    @Mapping(source = "debutId", target = "debut")
    @Mapping(source = "finId", target = "fin")
    PeriodeAnnee toEntity(PeriodeAnneeDTO periodeAnneeDTO);

    default PeriodeAnnee fromId(Long id) {
        if (id == null) {
            return null;
        }
        PeriodeAnnee periodeAnnee = new PeriodeAnnee();
        periodeAnnee.setId(id);
        return periodeAnnee;
    }
}
