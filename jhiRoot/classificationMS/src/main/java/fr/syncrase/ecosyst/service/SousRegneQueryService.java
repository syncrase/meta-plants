package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousRegne;
import fr.syncrase.ecosyst.repository.SousRegneRepository;
import fr.syncrase.ecosyst.service.criteria.SousRegneCriteria;
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
 * Service for executing complex queries for {@link SousRegne} entities in the database.
 * The main input is a {@link SousRegneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousRegne} or a {@link Page} of {@link SousRegne} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousRegneQueryService extends QueryService<SousRegne> {

    private final Logger log = LoggerFactory.getLogger(SousRegneQueryService.class);

    private final SousRegneRepository sousRegneRepository;

    public SousRegneQueryService(SousRegneRepository sousRegneRepository) {
        this.sousRegneRepository = sousRegneRepository;
    }

    /**
     * Return a {@link List} of {@link SousRegne} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousRegne> findByCriteria(SousRegneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousRegne> specification = createSpecification(criteria);
        return sousRegneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousRegne} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousRegne> findByCriteria(SousRegneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousRegne> specification = createSpecification(criteria);
        return sousRegneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousRegneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousRegne> specification = createSpecification(criteria);
        return sousRegneRepository.count(specification);
    }

    /**
     * Function to convert {@link SousRegneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousRegne> createSpecification(SousRegneCriteria criteria) {
        Specification<SousRegne> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousRegne_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousRegne_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousRegne_.nomLatin));
            }
            if (criteria.getRameausId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRameausId(), root -> root.join(SousRegne_.rameaus, JoinType.LEFT).get(Rameau_.id))
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousRegne_.synonymes, JoinType.LEFT).get(SousRegne_.id)
                        )
                    );
            }
            if (criteria.getRegneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRegneId(), root -> root.join(SousRegne_.regne, JoinType.LEFT).get(Regne_.id))
                    );
            }
            if (criteria.getSousRegneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousRegneId(),
                            root -> root.join(SousRegne_.sousRegne, JoinType.LEFT).get(SousRegne_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
