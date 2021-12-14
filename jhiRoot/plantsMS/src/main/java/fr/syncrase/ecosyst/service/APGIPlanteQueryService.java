package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.APGIPlante;
import fr.syncrase.ecosyst.repository.APGIPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.APGIPlanteCriteria;
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
 * Service for executing complex queries for {@link APGIPlante} entities in the database.
 * The main input is a {@link APGIPlanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGIPlante} or a {@link Page} of {@link APGIPlante} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIPlanteQueryService extends QueryService<APGIPlante> {

    private final Logger log = LoggerFactory.getLogger(APGIPlanteQueryService.class);

    private final APGIPlanteRepository aPGIPlanteRepository;

    public APGIPlanteQueryService(APGIPlanteRepository aPGIPlanteRepository) {
        this.aPGIPlanteRepository = aPGIPlanteRepository;
    }

    /**
     * Return a {@link List} of {@link APGIPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGIPlante> findByCriteria(APGIPlanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGIPlante> specification = createSpecification(criteria);
        return aPGIPlanteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link APGIPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIPlante> findByCriteria(APGIPlanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGIPlante> specification = createSpecification(criteria);
        return aPGIPlanteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(APGIPlanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<APGIPlante> specification = createSpecification(criteria);
        return aPGIPlanteRepository.count(specification);
    }

    /**
     * Function to convert {@link APGIPlanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<APGIPlante> createSpecification(APGIPlanteCriteria criteria) {
        Specification<APGIPlante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), APGIPlante_.id));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), APGIPlante_.ordre));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), APGIPlante_.famille));
            }
        }
        return specification;
    }
}
