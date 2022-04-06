package fr.syncrase.ecosyst.service.database;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.classification.entities.database.ClassificationNom;
import fr.syncrase.ecosyst.domain.classification.entities.database.ClassificationNom_;
import fr.syncrase.ecosyst.domain.classification.entities.database.CronquistRank_;
import fr.syncrase.ecosyst.repository.database.ClassificationNomRepository;
import fr.syncrase.ecosyst.service.criteria.ClassificationNomCriteria;
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
 * Service for executing complex queries for {@link ClassificationNom} entities in the database.
 * The main input is a {@link ClassificationNomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassificationNom} or a {@link Page} of {@link ClassificationNom} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassificationNomQueryService extends QueryService<ClassificationNom> {

    private final Logger log = LoggerFactory.getLogger(ClassificationNomQueryService.class);

    private final ClassificationNomRepository classificationNomRepository;

    public ClassificationNomQueryService(ClassificationNomRepository classificationNomRepository) {
        this.classificationNomRepository = classificationNomRepository;
    }

    /**
     * Return a {@link List} of {@link ClassificationNom} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassificationNom> findByCriteria(ClassificationNomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassificationNom> specification = createSpecification(criteria);
        return classificationNomRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ClassificationNom} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassificationNom> findByCriteria(ClassificationNomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassificationNom> specification = createSpecification(criteria);
        return classificationNomRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassificationNomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassificationNom> specification = createSpecification(criteria);
        return classificationNomRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassificationNomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassificationNom> createSpecification(ClassificationNomCriteria criteria) {
        Specification<ClassificationNom> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassificationNom_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), ClassificationNom_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), ClassificationNom_.nomLatin));
            }
            if (criteria.getCronquistRankId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCronquistRankId(),
                            root -> root.join(ClassificationNom_.cronquistRank, JoinType.LEFT).get(CronquistRank_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
