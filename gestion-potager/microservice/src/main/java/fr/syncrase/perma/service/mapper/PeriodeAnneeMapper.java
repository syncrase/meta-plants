package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.PeriodeAnnee;
import fr.syncrase.perma.service.dto.PeriodeAnneeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PeriodeAnnee} and its DTO {@link PeriodeAnneeDTO}.
 */
@Mapper(componentModel = "spring", uses = { MoisMapper.class })
public interface PeriodeAnneeMapper extends EntityMapper<PeriodeAnneeDTO, PeriodeAnnee> {
    @Mapping(target = "debut", source = "debut", qualifiedByName = "nom")
    @Mapping(target = "fin", source = "fin", qualifiedByName = "nom")
    PeriodeAnneeDTO toDto(PeriodeAnnee s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PeriodeAnneeDTO toDtoId(PeriodeAnnee periodeAnnee);
}
