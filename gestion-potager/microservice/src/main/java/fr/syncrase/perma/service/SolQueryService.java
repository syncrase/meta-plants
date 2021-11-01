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

import fr.syncrase.perma.domain.Sol;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.SolRepository;
import fr.syncrase.perma.service.dto.SolCriteria;
import fr.syncrase.perma.service.dto.SolDTO;
import fr.syncrase.perma.service.mapper.SolMapper;

/**
 * Service for executing complex queries for {@link Sol} entities in the database.
 * The main input is a {@link SolCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SolDTO} or a {@link Page} of {@link SolDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SolQueryService extends QueryService<Sol> {

    private final Logger log = LoggerFactory.getLogger(SolQueryService.class);

    private final SolRepository solRepository;

    private final SolMapper solMapper;

    public SolQueryService(SolRepository solRepository, SolMapper solMapper) {
        this.solRepository = solRepository;
        this.solMapper = solMapper;
    }

    /**
     * Return a {@link List} of {@link SolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SolDTO> findByCriteria(SolCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sol> specification = createSpecification(criteria);
        return solMapper.toDto(solRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SolDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SolDTO> findByCriteria(SolCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sol> specification = createSpecification(criteria);
        return solRepository.findAll(specification, page)
            .map(solMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SolCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sol> specification = createSpecification(criteria);
        return solRepository.count(specification);
    }

    /**
     * Function to convert {@link SolCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sol> createSpecification(SolCriteria criteria) {
        Specification<Sol> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sol_.id));
            }
            if (criteria.getAcidite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAcidite(), Sol_.acidite));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Sol_.type));
            }
        }
        return specification;
    }
}
