package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SuperRegne;
import fr.syncrase.ecosyst.repository.SuperRegneRepository;
import fr.syncrase.ecosyst.service.criteria.SuperRegneCriteria;
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
 * Service for executing complex queries for {@link SuperRegne} entities in the database.
 * The main input is a {@link SuperRegneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SuperRegne} or a {@link Page} of {@link SuperRegne} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuperRegneQueryService extends QueryService<SuperRegne> {

    private final Logger log = LoggerFactory.getLogger(SuperRegneQueryService.class);

    private final SuperRegneRepository superRegneRepository;

    public SuperRegneQueryService(SuperRegneRepository superRegneRepository) {
        this.superRegneRepository = superRegneRepository;
    }

    /**
     * Return a {@link List} of {@link SuperRegne} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SuperRegne> findByCriteria(SuperRegneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SuperRegne> specification = createSpecification(criteria);
        return superRegneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SuperRegne} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SuperRegne> findByCriteria(SuperRegneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SuperRegne> specification = createSpecification(criteria);
        return superRegneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuperRegneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SuperRegne> specification = createSpecification(criteria);
        return superRegneRepository.count(specification);
    }

    /**
     * Function to convert {@link SuperRegneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SuperRegne> createSpecification(SuperRegneCriteria criteria) {
        Specification<SuperRegne> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SuperRegne_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SuperRegne_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SuperRegne_.nomLatin));
            }
            if (criteria.getRegnesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRegnesId(), root -> root.join(SuperRegne_.regnes, JoinType.LEFT).get(Regne_.id))
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SuperRegne_.synonymes, JoinType.LEFT).get(SuperRegne_.id)
                        )
                    );
            }
            if (criteria.getSuperRegneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperRegneId(),
                            root -> root.join(SuperRegne_.superRegne, JoinType.LEFT).get(SuperRegne_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
