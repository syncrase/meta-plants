package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.InfraEmbranchement;
import fr.syncrase.ecosyst.repository.InfraEmbranchementRepository;
import fr.syncrase.ecosyst.service.criteria.InfraEmbranchementCriteria;
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
 * Service for executing complex queries for {@link InfraEmbranchement} entities in the database.
 * The main input is a {@link InfraEmbranchementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InfraEmbranchement} or a {@link Page} of {@link InfraEmbranchement} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InfraEmbranchementQueryService extends QueryService<InfraEmbranchement> {

    private final Logger log = LoggerFactory.getLogger(InfraEmbranchementQueryService.class);

    private final InfraEmbranchementRepository infraEmbranchementRepository;

    public InfraEmbranchementQueryService(InfraEmbranchementRepository infraEmbranchementRepository) {
        this.infraEmbranchementRepository = infraEmbranchementRepository;
    }

    /**
     * Return a {@link List} of {@link InfraEmbranchement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InfraEmbranchement> findByCriteria(InfraEmbranchementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InfraEmbranchement> specification = createSpecification(criteria);
        return infraEmbranchementRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InfraEmbranchement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InfraEmbranchement> findByCriteria(InfraEmbranchementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InfraEmbranchement> specification = createSpecification(criteria);
        return infraEmbranchementRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InfraEmbranchementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InfraEmbranchement> specification = createSpecification(criteria);
        return infraEmbranchementRepository.count(specification);
    }

    /**
     * Function to convert {@link InfraEmbranchementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InfraEmbranchement> createSpecification(InfraEmbranchementCriteria criteria) {
        Specification<InfraEmbranchement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InfraEmbranchement_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), InfraEmbranchement_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), InfraEmbranchement_.nomLatin));
            }
            if (criteria.getMicroEmbranchementsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMicroEmbranchementsId(),
                            root -> root.join(InfraEmbranchement_.microEmbranchements, JoinType.LEFT).get(MicroEmbranchement_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(InfraEmbranchement_.synonymes, JoinType.LEFT).get(InfraEmbranchement_.id)
                        )
                    );
            }
            if (criteria.getSousDivisionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousDivisionId(),
                            root -> root.join(InfraEmbranchement_.sousDivision, JoinType.LEFT).get(SousDivision_.id)
                        )
                    );
            }
            if (criteria.getInfraEmbranchementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraEmbranchementId(),
                            root -> root.join(InfraEmbranchement_.infraEmbranchement, JoinType.LEFT).get(InfraEmbranchement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
