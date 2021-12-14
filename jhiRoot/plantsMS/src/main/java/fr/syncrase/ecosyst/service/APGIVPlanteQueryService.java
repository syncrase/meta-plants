package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.APGIVPlante;
import fr.syncrase.ecosyst.repository.APGIVPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.APGIVPlanteCriteria;
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
 * Service for executing complex queries for {@link APGIVPlante} entities in the database.
 * The main input is a {@link APGIVPlanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGIVPlante} or a {@link Page} of {@link APGIVPlante} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIVPlanteQueryService extends QueryService<APGIVPlante> {

    private final Logger log = LoggerFactory.getLogger(APGIVPlanteQueryService.class);

    private final APGIVPlanteRepository aPGIVPlanteRepository;

    public APGIVPlanteQueryService(APGIVPlanteRepository aPGIVPlanteRepository) {
        this.aPGIVPlanteRepository = aPGIVPlanteRepository;
    }

    /**
     * Return a {@link List} of {@link APGIVPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGIVPlante> findByCriteria(APGIVPlanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGIVPlante> specification = createSpecification(criteria);
        return aPGIVPlanteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link APGIVPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIVPlante> findByCriteria(APGIVPlanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGIVPlante> specification = createSpecification(criteria);
        return aPGIVPlanteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(APGIVPlanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<APGIVPlante> specification = createSpecification(criteria);
        return aPGIVPlanteRepository.count(specification);
    }

    /**
     * Function to convert {@link APGIVPlanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<APGIVPlante> createSpecification(APGIVPlanteCriteria criteria) {
        Specification<APGIVPlante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), APGIVPlante_.id));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), APGIVPlante_.ordre));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), APGIVPlante_.famille));
            }
        }
        return specification;
    }
}
