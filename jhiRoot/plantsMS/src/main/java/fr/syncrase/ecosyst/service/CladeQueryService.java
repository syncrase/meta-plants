package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Clade;
import fr.syncrase.ecosyst.repository.CladeRepository;
import fr.syncrase.ecosyst.service.criteria.CladeCriteria;
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
 * Service for executing complex queries for {@link Clade} entities in the database.
 * The main input is a {@link CladeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Clade} or a {@link Page} of {@link Clade} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CladeQueryService extends QueryService<Clade> {

    private final Logger log = LoggerFactory.getLogger(CladeQueryService.class);

    private final CladeRepository cladeRepository;

    public CladeQueryService(CladeRepository cladeRepository) {
        this.cladeRepository = cladeRepository;
    }

    /**
     * Return a {@link List} of {@link Clade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Clade> findByCriteria(CladeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Clade> specification = createSpecification(criteria);
        return cladeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Clade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Clade> findByCriteria(CladeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Clade> specification = createSpecification(criteria);
        return cladeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CladeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Clade> specification = createSpecification(criteria);
        return cladeRepository.count(specification);
    }

    /**
     * Function to convert {@link CladeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Clade> createSpecification(CladeCriteria criteria) {
        Specification<Clade> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Clade_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Clade_.nom));
            }
            if (criteria.getApgiiisId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getApgiiisId(), root -> root.join(Clade_.apgiiis, JoinType.LEFT).get(APGIIIPlante_.id))
                    );
            }
        }
        return specification;
    }
}
