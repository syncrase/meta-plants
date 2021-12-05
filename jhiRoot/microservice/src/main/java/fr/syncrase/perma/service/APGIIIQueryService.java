package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.APGIII;
import fr.syncrase.perma.repository.APGIIIRepository;
import fr.syncrase.perma.service.criteria.APGIIICriteria;
import fr.syncrase.perma.service.dto.APGIIIDTO;
import fr.syncrase.perma.service.mapper.APGIIIMapper;
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
 * Service for executing complex queries for {@link APGIII} entities in the database.
 * The main input is a {@link APGIIICriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGIIIDTO} or a {@link Page} of {@link APGIIIDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIIIQueryService extends QueryService<APGIII> {

    private final Logger log = LoggerFactory.getLogger(APGIIIQueryService.class);

    private final APGIIIRepository aPGIIIRepository;

    private final APGIIIMapper aPGIIIMapper;

    public APGIIIQueryService(APGIIIRepository aPGIIIRepository, APGIIIMapper aPGIIIMapper) {
        this.aPGIIIRepository = aPGIIIRepository;
        this.aPGIIIMapper = aPGIIIMapper;
    }

    /**
     * Return a {@link List} of {@link APGIIIDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGIIIDTO> findByCriteria(APGIIICriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGIII> specification = createSpecification(criteria);
        return aPGIIIMapper.toDto(aPGIIIRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link APGIIIDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIIIDTO> findByCriteria(APGIIICriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGIII> specification = createSpecification(criteria);
        return aPGIIIRepository.findAll(specification, page).map(aPGIIIMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(APGIIICriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<APGIII> specification = createSpecification(criteria);
        return aPGIIIRepository.count(specification);
    }

    /**
     * Function to convert {@link APGIIICriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<APGIII> createSpecification(APGIIICriteria criteria) {
        Specification<APGIII> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), APGIII_.id));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), APGIII_.ordre));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), APGIII_.famille));
            }
        }
        return specification;
    }
}
