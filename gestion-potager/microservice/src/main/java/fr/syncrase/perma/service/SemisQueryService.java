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

import fr.syncrase.perma.domain.Semis;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.SemisRepository;
import fr.syncrase.perma.service.dto.SemisCriteria;
import fr.syncrase.perma.service.dto.SemisDTO;
import fr.syncrase.perma.service.mapper.SemisMapper;

/**
 * Service for executing complex queries for {@link Semis} entities in the database.
 * The main input is a {@link SemisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SemisDTO} or a {@link Page} of {@link SemisDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SemisQueryService extends QueryService<Semis> {

    private final Logger log = LoggerFactory.getLogger(SemisQueryService.class);

    private final SemisRepository semisRepository;

    private final SemisMapper semisMapper;

    public SemisQueryService(SemisRepository semisRepository, SemisMapper semisMapper) {
        this.semisRepository = semisRepository;
        this.semisMapper = semisMapper;
    }

    /**
     * Return a {@link List} of {@link SemisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SemisDTO> findByCriteria(SemisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Semis> specification = createSpecification(criteria);
        return semisMapper.toDto(semisRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SemisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SemisDTO> findByCriteria(SemisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Semis> specification = createSpecification(criteria);
        return semisRepository.findAll(specification, page)
            .map(semisMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SemisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Semis> specification = createSpecification(criteria);
        return semisRepository.count(specification);
    }

    /**
     * Function to convert {@link SemisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Semis> createSpecification(SemisCriteria criteria) {
        Specification<Semis> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Semis_.id));
            }
            if (criteria.getSemisPleineTerreId() != null) {
                specification = specification.and(buildSpecification(criteria.getSemisPleineTerreId(),
                    root -> root.join(Semis_.semisPleineTerre, JoinType.LEFT).get(PeriodeAnnee_.id)));
            }
            if (criteria.getSemisSousAbrisId() != null) {
                specification = specification.and(buildSpecification(criteria.getSemisSousAbrisId(),
                    root -> root.join(Semis_.semisSousAbris, JoinType.LEFT).get(PeriodeAnnee_.id)));
            }
            if (criteria.getTypeSemisId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeSemisId(),
                    root -> root.join(Semis_.typeSemis, JoinType.LEFT).get(TypeSemis_.id)));
            }
            if (criteria.getGerminationId() != null) {
                specification = specification.and(buildSpecification(criteria.getGerminationId(),
                    root -> root.join(Semis_.germination, JoinType.LEFT).get(Germination_.id)));
            }
        }
        return specification;
    }
}
