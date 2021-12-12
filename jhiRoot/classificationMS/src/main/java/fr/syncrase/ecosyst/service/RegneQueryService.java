package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Regne;
import fr.syncrase.ecosyst.repository.RegneRepository;
import fr.syncrase.ecosyst.service.criteria.RegneCriteria;
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
 * Service for executing complex queries for {@link Regne} entities in the database.
 * The main input is a {@link RegneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Regne} or a {@link Page} of {@link Regne} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegneQueryService extends QueryService<Regne> {

    private final Logger log = LoggerFactory.getLogger(RegneQueryService.class);

    private final RegneRepository regneRepository;

    public RegneQueryService(RegneRepository regneRepository) {
        this.regneRepository = regneRepository;
    }

    /**
     * Return a {@link List} of {@link Regne} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Regne> findByCriteria(RegneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Regne> specification = createSpecification(criteria);
        return regneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Regne} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Regne> findByCriteria(RegneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Regne> specification = createSpecification(criteria);
        return regneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Regne> specification = createSpecification(criteria);
        return regneRepository.count(specification);
    }

    /**
     * Function to convert {@link RegneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Regne> createSpecification(RegneCriteria criteria) {
        Specification<Regne> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Regne_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Regne_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Regne_.nomLatin));
            }
            if (criteria.getSousRegnesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousRegnesId(),
                            root -> root.join(Regne_.sousRegnes, JoinType.LEFT).get(SousRegne_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSynonymesId(), root -> root.join(Regne_.synonymes, JoinType.LEFT).get(Regne_.id))
                    );
            }
            if (criteria.getSuperRegneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperRegneId(),
                            root -> root.join(Regne_.superRegne, JoinType.LEFT).get(SuperRegne_.id)
                        )
                    );
            }
            if (criteria.getRegneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRegneId(), root -> root.join(Regne_.regne, JoinType.LEFT).get(Regne_.id))
                    );
            }
        }
        return specification;
    }
}
