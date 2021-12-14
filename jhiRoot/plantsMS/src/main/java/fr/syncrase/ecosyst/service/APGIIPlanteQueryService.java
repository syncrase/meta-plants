package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.APGIIPlante;
import fr.syncrase.ecosyst.repository.APGIIPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.APGIIPlanteCriteria;
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
 * Service for executing complex queries for {@link APGIIPlante} entities in the database.
 * The main input is a {@link APGIIPlanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGIIPlante} or a {@link Page} of {@link APGIIPlante} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIIPlanteQueryService extends QueryService<APGIIPlante> {

    private final Logger log = LoggerFactory.getLogger(APGIIPlanteQueryService.class);

    private final APGIIPlanteRepository aPGIIPlanteRepository;

    public APGIIPlanteQueryService(APGIIPlanteRepository aPGIIPlanteRepository) {
        this.aPGIIPlanteRepository = aPGIIPlanteRepository;
    }

    /**
     * Return a {@link List} of {@link APGIIPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGIIPlante> findByCriteria(APGIIPlanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGIIPlante> specification = createSpecification(criteria);
        return aPGIIPlanteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link APGIIPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIIPlante> findByCriteria(APGIIPlanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGIIPlante> specification = createSpecification(criteria);
        return aPGIIPlanteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(APGIIPlanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<APGIIPlante> specification = createSpecification(criteria);
        return aPGIIPlanteRepository.count(specification);
    }

    /**
     * Function to convert {@link APGIIPlanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<APGIIPlante> createSpecification(APGIIPlanteCriteria criteria) {
        Specification<APGIIPlante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), APGIIPlante_.id));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), APGIIPlante_.ordre));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), APGIIPlante_.famille));
            }
        }
        return specification;
    }
}
