package fr.syncrase.perma.service.mapper;

import fr.syncrase.perma.domain.Classification;
import fr.syncrase.perma.service.dto.ClassificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Classification} and its DTO {@link ClassificationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { RaunkierMapper.class, CronquistMapper.class, APGIMapper.class, APGIIMapper.class, APGIIIMapper.class, APGIVMapper.class }
)
public interface ClassificationMapper extends EntityMapper<ClassificationDTO, Classification> {
    @Mapping(target = "raunkier", source = "raunkier", qualifiedByName = "id")
    @Mapping(target = "cronquist", source = "cronquist", qualifiedByName = "id")
    @Mapping(target = "apg1", source = "apg1", qualifiedByName = "id")
    @Mapping(target = "apg2", source = "apg2", qualifiedByName = "id")
    @Mapping(target = "apg3", source = "apg3", qualifiedByName = "id")
    @Mapping(target = "apg4", source = "apg4", qualifiedByName = "id")
    ClassificationDTO toDto(Classification s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassificationDTO toDtoId(Classification classification);
}
