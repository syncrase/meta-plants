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

import fr.syncrase.perma.domain.Raunkier;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.RaunkierRepository;
import fr.syncrase.perma.service.dto.RaunkierCriteria;
import fr.syncrase.perma.service.dto.RaunkierDTO;
import fr.syncrase.perma.service.mapper.RaunkierMapper;

/**
 * Service for executing complex queries for {@link Raunkier} entities in the database.
 * The main input is a {@link RaunkierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RaunkierDTO} or a {@link Page} of {@link RaunkierDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RaunkierQueryService extends QueryService<Raunkier> {

    private final Logger log = LoggerFactory.getLogger(RaunkierQueryService.class);

    private final RaunkierRepository raunkierRepository;

    private final RaunkierMapper raunkierMapper;

    public RaunkierQueryService(RaunkierRepository raunkierRepository, RaunkierMapper raunkierMapper) {
        this.raunkierRepository = raunkierRepository;
        this.raunkierMapper = raunkierMapper;
    }

    /**
     * Return a {@link List} of {@link RaunkierDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RaunkierDTO> findByCriteria(RaunkierCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Raunkier> specification = createSpecification(criteria);
        return raunkierMapper.toDto(raunkierRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RaunkierDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RaunkierDTO> findByCriteria(RaunkierCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Raunkier> specification = createSpecification(criteria);
        return raunkierRepository.findAll(specification, page)
            .map(raunkierMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RaunkierCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Raunkier> specification = createSpecification(criteria);
        return raunkierRepository.count(specification);
    }

    /**
     * Function to convert {@link RaunkierCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Raunkier> createSpecification(RaunkierCriteria criteria) {
        Specification<Raunkier> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Raunkier_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Raunkier_.type));
            }
        }
        return specification;
    }
}
