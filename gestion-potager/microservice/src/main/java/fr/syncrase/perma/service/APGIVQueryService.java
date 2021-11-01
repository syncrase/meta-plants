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

import fr.syncrase.perma.domain.APGIV;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.APGIVRepository;
import fr.syncrase.perma.service.dto.APGIVCriteria;
import fr.syncrase.perma.service.dto.APGIVDTO;
import fr.syncrase.perma.service.mapper.APGIVMapper;

/**
 * Service for executing complex queries for {@link APGIV} entities in the database.
 * The main input is a {@link APGIVCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGIVDTO} or a {@link Page} of {@link APGIVDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIVQueryService extends QueryService<APGIV> {

    private final Logger log = LoggerFactory.getLogger(APGIVQueryService.class);

    private final APGIVRepository aPGIVRepository;

    private final APGIVMapper aPGIVMapper;

    public APGIVQueryService(APGIVRepository aPGIVRepository, APGIVMapper aPGIVMapper) {
        this.aPGIVRepository = aPGIVRepository;
        this.aPGIVMapper = aPGIVMapper;
    }

    /**
     * Return a {@link List} of {@link APGIVDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGIVDTO> findByCriteria(APGIVCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGIV> specification = createSpecification(criteria);
        return aPGIVMapper.toDto(aPGIVRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link APGIVDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIVDTO> findByCriteria(APGIVCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGIV> specification = createSpecification(criteria);
        return aPGIVRepository.findAll(specification, page)
            .map(aPGIVMapper::toDto);
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
