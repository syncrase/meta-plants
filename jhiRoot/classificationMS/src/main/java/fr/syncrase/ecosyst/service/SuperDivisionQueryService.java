package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SuperDivision;
import fr.syncrase.ecosyst.repository.SuperDivisionRepository;
import fr.syncrase.ecosyst.service.criteria.SuperDivisionCriteria;
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
 * Service for executing complex queries for {@link SuperDivision} entities in the database.
 * The main input is a {@link SuperDivisionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SuperDivision} or a {@link Page} of {@link SuperDivision} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuperDivisionQueryService extends QueryService<SuperDivision> {

    private final Logger log = LoggerFactory.getLogger(SuperDivisionQueryService.class);

    private final SuperDivisionRepository superDivisionRepository;

    public SuperDivisionQueryService(SuperDivisionRepository superDivisionRepository) {
        this.superDivisionRepository = superDivisionRepository;
    }

    /**
     * Return a {@link List} of {@link SuperDivision} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SuperDivision> findByCriteria(SuperDivisionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SuperDivision> specification = createSpecification(criteria);
        return superDivisionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SuperDivision} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SuperDivision> findByCriteria(SuperDivisionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SuperDivision> specification = createSpecification(criteria);
        return superDivisionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuperDivisionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SuperDivision> specification = createSpecification(criteria);
        return superDivisionRepository.count(specification);
    }

    /**
     * Function to convert {@link SuperDivisionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SuperDivision> createSpecification(SuperDivisionCriteria criteria) {
        Specification<SuperDivision> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SuperDivision_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SuperDivision_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SuperDivision_.nomLatin));
            }
            if (criteria.getDivisionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDivisionsId(),
                            root -> root.join(SuperDivision_.divisions, JoinType.LEFT).get(Division_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SuperDivision_.synonymes, JoinType.LEFT).get(SuperDivision_.id)
                        )
                    );
            }
            if (criteria.getInfraRegneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraRegneId(),
                            root -> root.join(SuperDivision_.infraRegne, JoinType.LEFT).get(InfraRegne_.id)
                        )
                    );
            }
            if (criteria.getSuperDivisionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperDivisionId(),
                            root -> root.join(SuperDivision_.superDivision, JoinType.LEFT).get(SuperDivision_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
