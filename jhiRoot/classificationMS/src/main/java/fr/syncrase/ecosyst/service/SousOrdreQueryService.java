package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousOrdre;
import fr.syncrase.ecosyst.repository.SousOrdreRepository;
import fr.syncrase.ecosyst.service.criteria.SousOrdreCriteria;
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
 * Service for executing complex queries for {@link SousOrdre} entities in the database.
 * The main input is a {@link SousOrdreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousOrdre} or a {@link Page} of {@link SousOrdre} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousOrdreQueryService extends QueryService<SousOrdre> {

    private final Logger log = LoggerFactory.getLogger(SousOrdreQueryService.class);

    private final SousOrdreRepository sousOrdreRepository;

    public SousOrdreQueryService(SousOrdreRepository sousOrdreRepository) {
        this.sousOrdreRepository = sousOrdreRepository;
    }

    /**
     * Return a {@link List} of {@link SousOrdre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousOrdre> findByCriteria(SousOrdreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousOrdre> specification = createSpecification(criteria);
        return sousOrdreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousOrdre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousOrdre> findByCriteria(SousOrdreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousOrdre> specification = createSpecification(criteria);
        return sousOrdreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousOrdreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousOrdre> specification = createSpecification(criteria);
        return sousOrdreRepository.count(specification);
    }

    /**
     * Function to convert {@link SousOrdreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousOrdre> createSpecification(SousOrdreCriteria criteria) {
        Specification<SousOrdre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousOrdre_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousOrdre_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousOrdre_.nomLatin));
            }
            if (criteria.getInfraOrdresId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraOrdresId(),
                            root -> root.join(SousOrdre_.infraOrdres, JoinType.LEFT).get(InfraOrdre_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousOrdre_.synonymes, JoinType.LEFT).get(SousOrdre_.id)
                        )
                    );
            }
            if (criteria.getOrdreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrdreId(), root -> root.join(SousOrdre_.ordre, JoinType.LEFT).get(Ordre_.id))
                    );
            }
            if (criteria.getSousOrdreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousOrdreId(),
                            root -> root.join(SousOrdre_.sousOrdre, JoinType.LEFT).get(SousOrdre_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
