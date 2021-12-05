package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Classification;
import fr.syncrase.perma.repository.ClassificationRepository;
import fr.syncrase.perma.service.criteria.ClassificationCriteria;
import fr.syncrase.perma.service.dto.ClassificationDTO;
import fr.syncrase.perma.service.mapper.ClassificationMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Classification} entities in the database.
 * The main input is a {@link ClassificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassificationDTO} or a {@link Page} of {@link ClassificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassificationQueryService extends QueryService<Classification> {

    private final Logger log = LoggerFactory.getLogger(ClassificationQueryService.class);

    private final ClassificationRepository classificationRepository;

    private final ClassificationMapper classificationMapper;

    public ClassificationQueryService(ClassificationRepository classificationRepository, ClassificationMapper classificationMapper) {
        this.classificationRepository = classificationRepository;
        this.classificationMapper = classificationMapper;
    }

    /**
     * Return a {@link List} of {@link ClassificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassificationDTO> findByCriteria(ClassificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Classification> specification = createSpecification(criteria);
        return classificationMapper.toDto(classificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClassificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassificationDTO> findByCriteria(ClassificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Classification> specification = createSpecification(criteria);
        return classificationRepository.findAll(specification, page).map(classificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Classification> specification = createSpecification(criteria);
        return classificationRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Classification> createSpecification(ClassificationCriteria criteria) {
        Specification<Classification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Classification_.id));
            }
            if (criteria.getRaunkierId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRaunkierId(),
                            root -> root.join(Classification_.raunkier, JoinType.LEFT).get(Raunkier_.id)
                        )
                    );
            }
            if (criteria.getCronquistId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCronquistId(),
                            root -> root.join(Classification_.cronquist, JoinType.LEFT).get(Cronquist_.id)
                        )
                    );
            }
            if (criteria.getApg1Id() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getApg1Id(), root -> root.join(Classification_.apg1, JoinType.LEFT).get(APGI_.id))
                    );
            }
            if (criteria.getApg2Id() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getApg2Id(), root -> root.join(Classification_.apg2, JoinType.LEFT).get(APGII_.id))
                    );
            }
            if (criteria.getApg3Id() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getApg3Id(), root -> root.join(Classification_.apg3, JoinType.LEFT).get(APGIII_.id))
                    );
            }
            if (criteria.getApg4Id() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getApg4Id(), root -> root.join(Classification_.apg4, JoinType.LEFT).get(APGIV_.id))
                    );
            }
        }
        return specification;
    }
}
