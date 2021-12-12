package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.InfraRegne;
import fr.syncrase.ecosyst.repository.InfraRegneRepository;
import fr.syncrase.ecosyst.service.criteria.InfraRegneCriteria;
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
 * Service for executing complex queries for {@link InfraRegne} entities in the database.
 * The main input is a {@link InfraRegneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InfraRegne} or a {@link Page} of {@link InfraRegne} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InfraRegneQueryService extends QueryService<InfraRegne> {

    private final Logger log = LoggerFactory.getLogger(InfraRegneQueryService.class);

    private final InfraRegneRepository infraRegneRepository;

    public InfraRegneQueryService(InfraRegneRepository infraRegneRepository) {
        this.infraRegneRepository = infraRegneRepository;
    }

    /**
     * Return a {@link List} of {@link InfraRegne} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InfraRegne> findByCriteria(InfraRegneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InfraRegne> specification = createSpecification(criteria);
        return infraRegneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InfraRegne} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InfraRegne> findByCriteria(InfraRegneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InfraRegne> specification = createSpecification(criteria);
        return infraRegneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InfraRegneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InfraRegne> specification = createSpecification(criteria);
        return infraRegneRepository.count(specification);
    }

    /**
     * Function to convert {@link InfraRegneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InfraRegne> createSpecification(InfraRegneCriteria criteria) {
        Specification<InfraRegne> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InfraRegne_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), InfraRegne_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), InfraRegne_.nomLatin));
            }
            if (criteria.getSuperDivisionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperDivisionsId(),
                            root -> root.join(InfraRegne_.superDivisions, JoinType.LEFT).get(SuperDivision_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(InfraRegne_.synonymes, JoinType.LEFT).get(InfraRegne_.id)
                        )
                    );
            }
            if (criteria.getRameauId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRameauId(), root -> root.join(InfraRegne_.rameau, JoinType.LEFT).get(Rameau_.id))
                    );
            }
            if (criteria.getInfraRegneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraRegneId(),
                            root -> root.join(InfraRegne_.infraRegne, JoinType.LEFT).get(InfraRegne_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
