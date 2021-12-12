package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Division;
import fr.syncrase.ecosyst.repository.DivisionRepository;
import fr.syncrase.ecosyst.service.criteria.DivisionCriteria;
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
 * Service for executing complex queries for {@link Division} entities in the database.
 * The main input is a {@link DivisionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Division} or a {@link Page} of {@link Division} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DivisionQueryService extends QueryService<Division> {

    private final Logger log = LoggerFactory.getLogger(DivisionQueryService.class);

    private final DivisionRepository divisionRepository;

    public DivisionQueryService(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    /**
     * Return a {@link List} of {@link Division} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Division> findByCriteria(DivisionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Division> specification = createSpecification(criteria);
        return divisionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Division} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Division> findByCriteria(DivisionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Division> specification = createSpecification(criteria);
        return divisionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DivisionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Division> specification = createSpecification(criteria);
        return divisionRepository.count(specification);
    }

    /**
     * Function to convert {@link DivisionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Division> createSpecification(DivisionCriteria criteria) {
        Specification<Division> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Division_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Division_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Division_.nomLatin));
            }
            if (criteria.getSousDivisionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousDivisionsId(),
                            root -> root.join(Division_.sousDivisions, JoinType.LEFT).get(SousDivision_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(Division_.synonymes, JoinType.LEFT).get(Division_.id)
                        )
                    );
            }
            if (criteria.getSuperDivisionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperDivisionId(),
                            root -> root.join(Division_.superDivision, JoinType.LEFT).get(SuperDivision_.id)
                        )
                    );
            }
            if (criteria.getDivisionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDivisionId(), root -> root.join(Division_.division, JoinType.LEFT).get(Division_.id))
                    );
            }
        }
        return specification;
    }
}
