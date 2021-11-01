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

import fr.syncrase.perma.domain.APGII;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.APGIIRepository;
import fr.syncrase.perma.service.dto.APGIICriteria;
import fr.syncrase.perma.service.dto.APGIIDTO;
import fr.syncrase.perma.service.mapper.APGIIMapper;

/**
 * Service for executing complex queries for {@link APGII} entities in the database.
 * The main input is a {@link APGIICriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGIIDTO} or a {@link Page} of {@link APGIIDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIIQueryService extends QueryService<APGII> {

    private final Logger log = LoggerFactory.getLogger(APGIIQueryService.class);

    private final APGIIRepository aPGIIRepository;

    private final APGIIMapper aPGIIMapper;

    public APGIIQueryService(APGIIRepository aPGIIRepository, APGIIMapper aPGIIMapper) {
        this.aPGIIRepository = aPGIIRepository;
        this.aPGIIMapper = aPGIIMapper;
    }

    /**
     * Return a {@link List} of {@link APGIIDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGIIDTO> findByCriteria(APGIICriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGII> specification = createSpecification(criteria);
        return aPGIIMapper.toDto(aPGIIRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link APGIIDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIIDTO> findByCriteria(APGIICriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGII> specification = createSpecification(criteria);
        return aPGIIRepository.findAll(specification, page)
            .map(aPGIIMapper::toDto);
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
