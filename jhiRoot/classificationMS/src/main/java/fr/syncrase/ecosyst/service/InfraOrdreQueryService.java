package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.InfraOrdre;
import fr.syncrase.ecosyst.repository.InfraOrdreRepository;
import fr.syncrase.ecosyst.service.criteria.InfraOrdreCriteria;
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
 * Service for executing complex queries for {@link InfraOrdre} entities in the database.
 * The main input is a {@link InfraOrdreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InfraOrdre} or a {@link Page} of {@link InfraOrdre} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InfraOrdreQueryService extends QueryService<InfraOrdre> {

    private final Logger log = LoggerFactory.getLogger(InfraOrdreQueryService.class);

    private final InfraOrdreRepository infraOrdreRepository;

    public InfraOrdreQueryService(InfraOrdreRepository infraOrdreRepository) {
        this.infraOrdreRepository = infraOrdreRepository;
    }

    /**
     * Return a {@link List} of {@link InfraOrdre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InfraOrdre> findByCriteria(InfraOrdreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InfraOrdre> specification = createSpecification(criteria);
        return infraOrdreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InfraOrdre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InfraOrdre> findByCriteria(InfraOrdreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InfraOrdre> specification = createSpecification(criteria);
        return infraOrdreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InfraOrdreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InfraOrdre> specification = createSpecification(criteria);
        return infraOrdreRepository.count(specification);
    }

    /**
     * Function to convert {@link InfraOrdreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InfraOrdre> createSpecification(InfraOrdreCriteria criteria) {
        Specification<InfraOrdre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InfraOrdre_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), InfraOrdre_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), InfraOrdre_.nomLatin));
            }
            if (criteria.getMicroOrdresId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMicroOrdresId(),
                            root -> root.join(InfraOrdre_.microOrdres, JoinType.LEFT).get(MicroOrdre_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(InfraOrdre_.synonymes, JoinType.LEFT).get(InfraOrdre_.id)
                        )
                    );
            }
            if (criteria.getSousOrdreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousOrdreId(),
                            root -> root.join(InfraOrdre_.sousOrdre, JoinType.LEFT).get(SousOrdre_.id)
                        )
                    );
            }
            if (criteria.getInfraOrdreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraOrdreId(),
                            root -> root.join(InfraOrdre_.infraOrdre, JoinType.LEFT).get(InfraOrdre_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
