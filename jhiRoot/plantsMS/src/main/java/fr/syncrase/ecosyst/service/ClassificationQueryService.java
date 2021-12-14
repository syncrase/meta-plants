package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import fr.syncrase.ecosyst.service.criteria.ClassificationCriteria;
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
 * It returns a {@link List} of {@link Classification} or a {@link Page} of {@link Classification} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassificationQueryService extends QueryService<Classification> {

    private final Logger log = LoggerFactory.getLogger(ClassificationQueryService.class);

    private final ClassificationRepository classificationRepository;

    public ClassificationQueryService(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    /**
     * Return a {@link List} of {@link Classification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Classification> findByCriteria(ClassificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Classification> specification = createSpecification(criteria);
        return classificationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Classification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Classification> findByCriteria(ClassificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Classification> specification = createSpecification(criteria);
        return classificationRepository.findAll(specification, page);
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
                            root -> root.join(Classification_.raunkier, JoinType.LEFT).get(RaunkierPlante_.id)
                        )
                    );
            }
            if (criteria.getCronquistId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCronquistId(),
                            root -> root.join(Classification_.cronquist, JoinType.LEFT).get(CronquistPlante_.id)
                        )
                    );
            }
            if (criteria.getApg1Id() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getApg1Id(), root -> root.join(Classification_.apg1, JoinType.LEFT).get(APGIPlante_.id))
                    );
            }
            if (criteria.getApg2Id() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApg2Id(),
                            root -> root.join(Classification_.apg2, JoinType.LEFT).get(APGIIPlante_.id)
                        )
                    );
            }
            if (criteria.getApg3Id() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApg3Id(),
                            root -> root.join(Classification_.apg3, JoinType.LEFT).get(APGIIIPlante_.id)
                        )
                    );
            }
            if (criteria.getApg4Id() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApg4Id(),
                            root -> root.join(Classification_.apg4, JoinType.LEFT).get(APGIVPlante_.id)
                        )
                    );
            }
            if (criteria.getPlanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPlanteId(), root -> root.join(Classification_.plante, JoinType.LEFT).get(Plante_.id))
                    );
            }
        }
        return specification;
    }
}
