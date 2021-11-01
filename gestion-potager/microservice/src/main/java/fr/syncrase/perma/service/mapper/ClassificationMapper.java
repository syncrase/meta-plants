package fr.syncrase.perma.service.mapper;


import fr.syncrase.perma.domain.*;
import fr.syncrase.perma.service.dto.ClassificationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Classification} and its DTO {@link ClassificationDTO}.
 */
@Mapper(componentModel = "spring", uses = {RaunkierMapper.class, CronquistMapper.class, APGIMapper.class, APGIIMapper.class, APGIIIMapper.class, APGIVMapper.class})
public interface ClassificationMapper extends EntityMapper<ClassificationDTO, Classification> {

    @Mapping(source = "raunkier.id", target = "raunkierId")
    @Mapping(source = "cronquist.id", target = "cronquistId")
    @Mapping(source = "apg1.id", target = "apg1Id")
    @Mapping(source = "apg2.id", target = "apg2Id")
    @Mapping(source = "apg3.id", target = "apg3Id")
    @Mapping(source = "apg4.id", target = "apg4Id")
    ClassificationDTO toDto(Classification classification);

    @Mapping(source = "raunkierId", target = "raunkier")
    @Mapping(source = "cronquistId", target = "cronquist")
    @Mapping(source = "apg1Id", target = "apg1")
    @Mapping(source = "apg2Id", target = "apg2")
    @Mapping(source = "apg3Id", target = "apg3")
    @Mapping(source = "apg4Id", target = "apg4")
    Classification toEntity(ClassificationDTO classificationDTO);

    default Classification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Classification classification = new Classification();
        classification.setId(id);
        return classification;
    }
}
