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

import fr.syncrase.perma.domain.Ressemblance;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.RessemblanceRepository;
import fr.syncrase.perma.service.dto.RessemblanceCriteria;
import fr.syncrase.perma.service.dto.RessemblanceDTO;
import fr.syncrase.perma.service.mapper.RessemblanceMapper;

/**
 * Service for executing complex queries for {@link Ressemblance} entities in the database.
 * The main input is a {@link RessemblanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RessemblanceDTO} or a {@link Page} of {@link RessemblanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RessemblanceQueryService extends QueryService<Ressemblance> {

    private final Logger log = LoggerFactory.getLogger(RessemblanceQueryService.class);

    private final RessemblanceRepository ressemblanceRepository;

    private final RessemblanceMapper ressemblanceMapper;

    public RessemblanceQueryService(RessemblanceRepository ressemblanceRepository, RessemblanceMapper ressemblanceMapper) {
        this.ressemblanceRepository = ressemblanceRepository;
        this.ressemblanceMapper = ressemblanceMapper;
    }

    /**
     * Return a {@link List} of {@link RessemblanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RessemblanceDTO> findByCriteria(RessemblanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ressemblance> specification = createSpecification(criteria);
        return ressemblanceMapper.toDto(ressemblanceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RessemblanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RessemblanceDTO> findByCriteria(RessemblanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ressemblance> specification = createSpecification(criteria);
        return ressemblanceRepository.findAll(specification, page)
            .map(ressemblanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RessemblanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ressemblance> specification = createSpecification(criteria);
        return ressemblanceRepository.count(specification);
    }

    /**
     * Function to convert {@link RessemblanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ressemblance> createSpecification(RessemblanceCriteria criteria) {
        Specification<Ressemblance> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ressemblance_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Ressemblance_.description));
            }
            if (criteria.getConfusionId() != null) {
                specification = specification.and(buildSpecification(criteria.getConfusionId(),
                    root -> root.join(Ressemblance_.confusion, JoinType.LEFT).get(Plante_.id)));
            }
        }
        return specification;
    }
}
