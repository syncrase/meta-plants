package fr.syncrase.perma.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fr.syncrase.perma.domain.APGI;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.APGIRepository;
import fr.syncrase.perma.service.dto.APGICriteria;
import fr.syncrase.perma.service.dto.APGIDTO;
import fr.syncrase.perma.service.mapper.APGIMapper;

/**
 * Service for executing complex queries for {@link APGI} entities in the database.
 * The main input is a {@link APGICriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGIDTO} or a {@link Page} of {@link APGIDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIQueryService extends QueryService<APGI> {

    private final Logger log = LoggerFactory.getLogger(APGIQueryService.class);

    private final APGIRepository aPGIRepository;

    private final APGIMapper aPGIMapper;

    public APGIQueryService(APGIRepository aPGIRepository, APGIMapper aPGIMapper) {
        this.aPGIRepository = aPGIRepository;
        this.aPGIMapper = aPGIMapper;
    }

    /**
     * Return a {@link List} of {@link APGIDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGIDTO> findByCriteria(APGICriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGI> specification = createSpecification(criteria);
        return aPGIMapper.toDto(aPGIRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link APGIDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIDTO> findByCriteria(APGICriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGI> specification = createSpecification(criteria);
        return aPGIRepository.findAll(specification, page)
            .map(aPGIMapper::toDto);
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
