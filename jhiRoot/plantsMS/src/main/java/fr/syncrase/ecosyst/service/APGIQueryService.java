package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.APGI;
import fr.syncrase.ecosyst.repository.APGIRepository;
import fr.syncrase.ecosyst.service.criteria.APGICriteria;
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
 * Service for executing complex queries for {@link APGI} entities in the database.
 * The main input is a {@link APGICriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGI} or a {@link Page} of {@link APGI} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIQueryService extends QueryService<APGI> {

    private final Logger log = LoggerFactory.getLogger(APGIQueryService.class);

    private final APGIRepository aPGIRepository;

    public APGIQueryService(APGIRepository aPGIRepository) {
        this.aPGIRepository = aPGIRepository;
    }

    /**
     * Return a {@link List} of {@link APGI} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGI> findByCriteria(APGICriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGI> specification = createSpecification(criteria);
        return aPGIRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link APGI} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGI> findByCriteria(APGICriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGI> specification = createSpecification(criteria);
        return aPGIRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(APGICriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<APGI> specification = createSpecification(criteria);
        return aPGIRepository.count(specification);
    }

    /**
     * Function to convert {@link APGICriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<APGI> createSpecification(APGICriteria criteria) {
        Specification<APGI> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), APGI_.id));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), APGI_.ordre));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), APGI_.famille));
            }
        }
        return specification;
    }
}
