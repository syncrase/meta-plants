package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Strate;
import fr.syncrase.perma.repository.StrateRepository;
import fr.syncrase.perma.service.criteria.StrateCriteria;
import fr.syncrase.perma.service.dto.StrateDTO;
import fr.syncrase.perma.service.mapper.StrateMapper;
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
 * Service for executing complex queries for {@link Strate} entities in the database.
 * The main input is a {@link StrateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StrateDTO} or a {@link Page} of {@link StrateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StrateQueryService extends QueryService<Strate> {

    private final Logger log = LoggerFactory.getLogger(StrateQueryService.class);

    private final StrateRepository strateRepository;

    private final StrateMapper strateMapper;

    public StrateQueryService(StrateRepository strateRepository, StrateMapper strateMapper) {
        this.strateRepository = strateRepository;
        this.strateMapper = strateMapper;
    }

    /**
     * Return a {@link List} of {@link StrateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StrateDTO> findByCriteria(StrateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Strate> specification = createSpecification(criteria);
        return strateMapper.toDto(strateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StrateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StrateDTO> findByCriteria(StrateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Strate> specification = createSpecification(criteria);
        return strateRepository.findAll(specification, page).map(strateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StrateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Strate> specification = createSpecification(criteria);
        return strateRepository.count(specification);
    }

    /**
     * Function to convert {@link StrateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Strate> createSpecification(StrateCriteria criteria) {
        Specification<Strate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Strate_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Strate_.type));
            }
            if (criteria.getPlanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPlanteId(), root -> root.join(Strate_.plantes, JoinType.LEFT).get(Plante_.id))
                    );
            }
        }
        return specification;
    }
}
