package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.APGIV;
import fr.syncrase.ecosyst.repository.APGIVRepository;
import fr.syncrase.ecosyst.service.criteria.APGIVCriteria;
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
 * Service for executing complex queries for {@link APGIV} entities in the database.
 * The main input is a {@link APGIVCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGIV} or a {@link Page} of {@link APGIV} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIVQueryService extends QueryService<APGIV> {

    private final Logger log = LoggerFactory.getLogger(APGIVQueryService.class);

    private final APGIVRepository aPGIVRepository;

    public APGIVQueryService(APGIVRepository aPGIVRepository) {
        this.aPGIVRepository = aPGIVRepository;
    }

    /**
     * Return a {@link List} of {@link APGIV} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGIV> findByCriteria(APGIVCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGIV> specification = createSpecification(criteria);
        return aPGIVRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link APGIV} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIV> findByCriteria(APGIVCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGIV> specification = createSpecification(criteria);
        return aPGIVRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(APGIVCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<APGIV> specification = createSpecification(criteria);
        return aPGIVRepository.count(specification);
    }

    /**
     * Function to convert {@link APGIVCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<APGIV> createSpecification(APGIVCriteria criteria) {
        Specification<APGIV> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), APGIV_.id));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), APGIV_.ordre));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), APGIV_.famille));
            }
        }
        return specification;
    }
}
