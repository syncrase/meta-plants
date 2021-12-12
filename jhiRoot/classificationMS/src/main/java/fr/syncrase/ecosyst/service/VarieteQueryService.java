package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Variete;
import fr.syncrase.ecosyst.repository.VarieteRepository;
import fr.syncrase.ecosyst.service.criteria.VarieteCriteria;
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
 * Service for executing complex queries for {@link Variete} entities in the database.
 * The main input is a {@link VarieteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Variete} or a {@link Page} of {@link Variete} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VarieteQueryService extends QueryService<Variete> {

    private final Logger log = LoggerFactory.getLogger(VarieteQueryService.class);

    private final VarieteRepository varieteRepository;

    public VarieteQueryService(VarieteRepository varieteRepository) {
        this.varieteRepository = varieteRepository;
    }

    /**
     * Return a {@link List} of {@link Variete} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Variete> findByCriteria(VarieteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Variete> specification = createSpecification(criteria);
        return varieteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Variete} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Variete> findByCriteria(VarieteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Variete> specification = createSpecification(criteria);
        return varieteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VarieteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Variete> specification = createSpecification(criteria);
        return varieteRepository.count(specification);
    }

    /**
     * Function to convert {@link VarieteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Variete> createSpecification(VarieteCriteria criteria) {
        Specification<Variete> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Variete_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Variete_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Variete_.nomLatin));
            }
            if (criteria.getSousVarietesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousVarietesId(),
                            root -> root.join(Variete_.sousVarietes, JoinType.LEFT).get(SousVariete_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSynonymesId(), root -> root.join(Variete_.synonymes, JoinType.LEFT).get(Variete_.id))
                    );
            }
            if (criteria.getSousEspeceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousEspeceId(),
                            root -> root.join(Variete_.sousEspece, JoinType.LEFT).get(SousEspece_.id)
                        )
                    );
            }
            if (criteria.getVarieteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVarieteId(), root -> root.join(Variete_.variete, JoinType.LEFT).get(Variete_.id))
                    );
            }
        }
        return specification;
    }
}
