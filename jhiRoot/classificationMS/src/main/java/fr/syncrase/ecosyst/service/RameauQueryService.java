package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Rameau;
import fr.syncrase.ecosyst.repository.RameauRepository;
import fr.syncrase.ecosyst.service.criteria.RameauCriteria;
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
 * Service for executing complex queries for {@link Rameau} entities in the database.
 * The main input is a {@link RameauCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Rameau} or a {@link Page} of {@link Rameau} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RameauQueryService extends QueryService<Rameau> {

    private final Logger log = LoggerFactory.getLogger(RameauQueryService.class);

    private final RameauRepository rameauRepository;

    public RameauQueryService(RameauRepository rameauRepository) {
        this.rameauRepository = rameauRepository;
    }

    /**
     * Return a {@link List} of {@link Rameau} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Rameau> findByCriteria(RameauCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Rameau> specification = createSpecification(criteria);
        return rameauRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Rameau} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Rameau> findByCriteria(RameauCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rameau> specification = createSpecification(criteria);
        return rameauRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RameauCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rameau> specification = createSpecification(criteria);
        return rameauRepository.count(specification);
    }

    /**
     * Function to convert {@link RameauCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rameau> createSpecification(RameauCriteria criteria) {
        Specification<Rameau> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rameau_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Rameau_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Rameau_.nomLatin));
            }
            if (criteria.getInfraRegnesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraRegnesId(),
                            root -> root.join(Rameau_.infraRegnes, JoinType.LEFT).get(InfraRegne_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSynonymesId(), root -> root.join(Rameau_.synonymes, JoinType.LEFT).get(Rameau_.id))
                    );
            }
            if (criteria.getSousRegneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousRegneId(),
                            root -> root.join(Rameau_.sousRegne, JoinType.LEFT).get(SousRegne_.id)
                        )
                    );
            }
            if (criteria.getRameauId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRameauId(), root -> root.join(Rameau_.rameau, JoinType.LEFT).get(Rameau_.id))
                    );
            }
        }
        return specification;
    }
}
