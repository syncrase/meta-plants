package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.RaunkierPlante;
import fr.syncrase.ecosyst.repository.RaunkierPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.RaunkierPlanteCriteria;
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
 * Service for executing complex queries for {@link RaunkierPlante} entities in the database.
 * The main input is a {@link RaunkierPlanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RaunkierPlante} or a {@link Page} of {@link RaunkierPlante} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RaunkierPlanteQueryService extends QueryService<RaunkierPlante> {

    private final Logger log = LoggerFactory.getLogger(RaunkierPlanteQueryService.class);

    private final RaunkierPlanteRepository raunkierPlanteRepository;

    public RaunkierPlanteQueryService(RaunkierPlanteRepository raunkierPlanteRepository) {
        this.raunkierPlanteRepository = raunkierPlanteRepository;
    }

    /**
     * Return a {@link List} of {@link RaunkierPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RaunkierPlante> findByCriteria(RaunkierPlanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RaunkierPlante> specification = createSpecification(criteria);
        return raunkierPlanteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RaunkierPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RaunkierPlante> findByCriteria(RaunkierPlanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RaunkierPlante> specification = createSpecification(criteria);
        return raunkierPlanteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RaunkierPlanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RaunkierPlante> specification = createSpecification(criteria);
        return raunkierPlanteRepository.count(specification);
    }

    /**
     * Function to convert {@link RaunkierPlanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RaunkierPlante> createSpecification(RaunkierPlanteCriteria criteria) {
        Specification<RaunkierPlante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RaunkierPlante_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), RaunkierPlante_.type));
            }
        }
        return specification;
    }
}
