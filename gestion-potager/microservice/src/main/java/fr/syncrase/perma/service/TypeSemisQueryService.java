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

import fr.syncrase.perma.domain.TypeSemis;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.TypeSemisRepository;
import fr.syncrase.perma.service.dto.TypeSemisCriteria;
import fr.syncrase.perma.service.dto.TypeSemisDTO;
import fr.syncrase.perma.service.mapper.TypeSemisMapper;

/**
 * Service for executing complex queries for {@link TypeSemis} entities in the database.
 * The main input is a {@link TypeSemisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypeSemisDTO} or a {@link Page} of {@link TypeSemisDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeSemisQueryService extends QueryService<TypeSemis> {

    private final Logger log = LoggerFactory.getLogger(TypeSemisQueryService.class);

    private final TypeSemisRepository typeSemisRepository;

    private final TypeSemisMapper typeSemisMapper;

    public TypeSemisQueryService(TypeSemisRepository typeSemisRepository, TypeSemisMapper typeSemisMapper) {
        this.typeSemisRepository = typeSemisRepository;
        this.typeSemisMapper = typeSemisMapper;
    }

    /**
     * Return a {@link List} of {@link TypeSemisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypeSemisDTO> findByCriteria(TypeSemisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypeSemis> specification = createSpecification(criteria);
        return typeSemisMapper.toDto(typeSemisRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TypeSemisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeSemisDTO> findByCriteria(TypeSemisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypeSemis> specification = createSpecification(criteria);
        return typeSemisRepository.findAll(specification, page)
            .map(typeSemisMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeSemisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypeSemis> specification = createSpecification(criteria);
        return typeSemisRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeSemisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeSemis> createSpecification(TypeSemisCriteria criteria) {
        Specification<TypeSemis> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TypeSemis_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TypeSemis_.description));
            }
        }
        return specification;
    }
}