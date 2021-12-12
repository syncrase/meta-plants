package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousDivision;
import fr.syncrase.ecosyst.repository.SousDivisionRepository;
import fr.syncrase.ecosyst.service.criteria.SousDivisionCriteria;
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
 * Service for executing complex queries for {@link SousDivision} entities in the database.
 * The main input is a {@link SousDivisionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousDivision} or a {@link Page} of {@link SousDivision} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousDivisionQueryService extends QueryService<SousDivision> {

    private final Logger log = LoggerFactory.getLogger(SousDivisionQueryService.class);

    private final SousDivisionRepository sousDivisionRepository;

    public SousDivisionQueryService(SousDivisionRepository sousDivisionRepository) {
        this.sousDivisionRepository = sousDivisionRepository;
    }

    /**
     * Return a {@link List} of {@link SousDivision} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousDivision> findByCriteria(SousDivisionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousDivision> specification = createSpecification(criteria);
        return sousDivisionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousDivision} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousDivision> findByCriteria(SousDivisionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousDivision> specification = createSpecification(criteria);
        return sousDivisionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousDivisionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousDivision> specification = createSpecification(criteria);
        return sousDivisionRepository.count(specification);
    }

    /**
     * Function to convert {@link SousDivisionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousDivision> createSpecification(SousDivisionCriteria criteria) {
        Specification<SousDivision> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousDivision_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousDivision_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousDivision_.nomLatin));
            }
            if (criteria.getInfraEmbranchementsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraEmbranchementsId(),
                            root -> root.join(SousDivision_.infraEmbranchements, JoinType.LEFT).get(InfraEmbranchement_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousDivision_.synonymes, JoinType.LEFT).get(SousDivision_.id)
                        )
                    );
            }
            if (criteria.getDivisionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDivisionId(),
                            root -> root.join(SousDivision_.division, JoinType.LEFT).get(Division_.id)
                        )
                    );
            }
            if (criteria.getSousDivisionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousDivisionId(),
                            root -> root.join(SousDivision_.sousDivision, JoinType.LEFT).get(SousDivision_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
