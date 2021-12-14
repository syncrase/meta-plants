package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.APGIIIPlante;
import fr.syncrase.ecosyst.repository.APGIIIPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.APGIIIPlanteCriteria;
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
 * Service for executing complex queries for {@link APGIIIPlante} entities in the database.
 * The main input is a {@link APGIIIPlanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link APGIIIPlante} or a {@link Page} of {@link APGIIIPlante} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class APGIIIPlanteQueryService extends QueryService<APGIIIPlante> {

    private final Logger log = LoggerFactory.getLogger(APGIIIPlanteQueryService.class);

    private final APGIIIPlanteRepository aPGIIIPlanteRepository;

    public APGIIIPlanteQueryService(APGIIIPlanteRepository aPGIIIPlanteRepository) {
        this.aPGIIIPlanteRepository = aPGIIIPlanteRepository;
    }

    /**
     * Return a {@link List} of {@link APGIIIPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<APGIIIPlante> findByCriteria(APGIIIPlanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<APGIIIPlante> specification = createSpecification(criteria);
        return aPGIIIPlanteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link APGIIIPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<APGIIIPlante> findByCriteria(APGIIIPlanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<APGIIIPlante> specification = createSpecification(criteria);
        return aPGIIIPlanteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(APGIIIPlanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<APGIIIPlante> specification = createSpecification(criteria);
        return aPGIIIPlanteRepository.count(specification);
    }

    /**
     * Function to convert {@link APGIIIPlanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<APGIIIPlante> createSpecification(APGIIIPlanteCriteria criteria) {
        Specification<APGIIIPlante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), APGIIIPlante_.id));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), APGIIIPlante_.ordre));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), APGIIIPlante_.famille));
            }
            if (criteria.getSousFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousFamille(), APGIIIPlante_.sousFamille));
            }
            if (criteria.getTribu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTribu(), APGIIIPlante_.tribu));
            }
            if (criteria.getSousTribu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousTribu(), APGIIIPlante_.sousTribu));
            }
            if (criteria.getGenre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenre(), APGIIIPlante_.genre));
            }
            if (criteria.getCladesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCladesId(), root -> root.join(APGIIIPlante_.clades, JoinType.LEFT).get(Clade_.id))
                    );
            }
        }
        return specification;
    }
}
