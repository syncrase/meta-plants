package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.APGII;
import fr.syncrase.ecosyst.repository.APGIIRepository;
import fr.syncrase.ecosyst.service.criteria.APGIICriteria;
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
 * Service for executing complex queries for {@link APGII} entities in the database.
 * The main input is a {@link APGIICriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGII} or a {@link Page} of {@link APGII} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIIQueryService extends QueryService<APGII> {

    private final Logger log = LoggerFactory.getLogger(APGIIQueryService.class);

    private final APGIIRepository aPGIIRepository;

    public APGIIQueryService(APGIIRepository aPGIIRepository) {
        this.aPGIIRepository = aPGIIRepository;
    }

    /**
     * Return a {@link List} of {@link APGII} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGII> findByCriteria(APGIICriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGII> specification = createSpecification(criteria);
        return aPGIIRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link APGII} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGII> findByCriteria(APGIICriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGII> specification = createSpecification(criteria);
        return aPGIIRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(APGIICriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<APGII> specification = createSpecification(criteria);
        return aPGIIRepository.count(specification);
    }

    /**
     * Function to convert {@link APGIICriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<APGII> createSpecification(APGIICriteria criteria) {
        Specification<APGII> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), APGII_.id));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), APGII_.ordre));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), APGII_.famille));
            }
        }
        return specification;
    }
}
