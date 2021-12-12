package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousVariete;
import fr.syncrase.ecosyst.repository.SousVarieteRepository;
import fr.syncrase.ecosyst.service.criteria.SousVarieteCriteria;
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
 * Service for executing complex queries for {@link SousVariete} entities in the database.
 * The main input is a {@link SousVarieteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousVariete} or a {@link Page} of {@link SousVariete} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousVarieteQueryService extends QueryService<SousVariete> {

    private final Logger log = LoggerFactory.getLogger(SousVarieteQueryService.class);

    private final SousVarieteRepository sousVarieteRepository;

    public SousVarieteQueryService(SousVarieteRepository sousVarieteRepository) {
        this.sousVarieteRepository = sousVarieteRepository;
    }

    /**
     * Return a {@link List} of {@link SousVariete} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousVariete> findByCriteria(SousVarieteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousVariete> specification = createSpecification(criteria);
        return sousVarieteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousVariete} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousVariete> findByCriteria(SousVarieteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousVariete> specification = createSpecification(criteria);
        return sousVarieteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousVarieteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousVariete> specification = createSpecification(criteria);
        return sousVarieteRepository.count(specification);
    }

    /**
     * Function to convert {@link SousVarieteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousVariete> createSpecification(SousVarieteCriteria criteria) {
        Specification<SousVariete> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousVariete_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousVariete_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousVariete_.nomLatin));
            }
            if (criteria.getFormesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFormesId(), root -> root.join(SousVariete_.formes, JoinType.LEFT).get(Forme_.id))
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousVariete_.synonymes, JoinType.LEFT).get(SousVariete_.id)
                        )
                    );
            }
            if (criteria.getVarieteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVarieteId(), root -> root.join(SousVariete_.variete, JoinType.LEFT).get(Variete_.id))
                    );
            }
            if (criteria.getSousVarieteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousVarieteId(),
                            root -> root.join(SousVariete_.sousVariete, JoinType.LEFT).get(SousVariete_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
