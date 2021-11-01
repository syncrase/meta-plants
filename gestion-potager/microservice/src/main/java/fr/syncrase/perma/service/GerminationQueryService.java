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

import fr.syncrase.perma.domain.Germination;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.GerminationRepository;
import fr.syncrase.perma.service.dto.GerminationCriteria;
import fr.syncrase.perma.service.dto.GerminationDTO;
import fr.syncrase.perma.service.mapper.GerminationMapper;

/**
 * Service for executing complex queries for {@link Germination} entities in the database.
 * The main input is a {@link GerminationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GerminationDTO} or a {@link Page} of {@link GerminationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GerminationQueryService extends QueryService<Germination> {

    private final Logger log = LoggerFactory.getLogger(GerminationQueryService.class);

    private final GerminationRepository germinationRepository;

    private final GerminationMapper germinationMapper;

    public GerminationQueryService(GerminationRepository germinationRepository, GerminationMapper germinationMapper) {
        this.germinationRepository = germinationRepository;
        this.germinationMapper = germinationMapper;
    }

    /**
     * Return a {@link List} of {@link GerminationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GerminationDTO> findByCriteria(GerminationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Germination> specification = createSpecification(criteria);
        return germinationMapper.toDto(germinationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GerminationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GerminationDTO> findByCriteria(GerminationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Germination> specification = createSpecification(criteria);
        return germinationRepository.findAll(specification, page)
            .map(germinationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GerminationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Germination> specification = createSpecification(criteria);
        return germinationRepository.count(specification);
    }

    /**
     * Function to convert {@link GerminationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Germination> createSpecification(GerminationCriteria criteria) {
        Specification<Germination> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Germination_.id));
            }
            if (criteria.getTempsDeGermination() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTempsDeGermination(), Germination_.tempsDeGermination));
            }
            if (criteria.getConditionDeGermination() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConditionDeGermination(), Germination_.conditionDeGermination));
            }
        }
        return specification;
    }
}
