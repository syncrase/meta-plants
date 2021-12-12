package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.InfraClasse;
import fr.syncrase.ecosyst.repository.InfraClasseRepository;
import fr.syncrase.ecosyst.service.criteria.InfraClasseCriteria;
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
 * Service for executing complex queries for {@link InfraClasse} entities in the database.
 * The main input is a {@link InfraClasseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InfraClasse} or a {@link Page} of {@link InfraClasse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InfraClasseQueryService extends QueryService<InfraClasse> {

    private final Logger log = LoggerFactory.getLogger(InfraClasseQueryService.class);

    private final InfraClasseRepository infraClasseRepository;

    public InfraClasseQueryService(InfraClasseRepository infraClasseRepository) {
        this.infraClasseRepository = infraClasseRepository;
    }

    /**
     * Return a {@link List} of {@link InfraClasse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InfraClasse> findByCriteria(InfraClasseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InfraClasse> specification = createSpecification(criteria);
        return infraClasseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InfraClasse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InfraClasse> findByCriteria(InfraClasseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InfraClasse> specification = createSpecification(criteria);
        return infraClasseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InfraClasseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InfraClasse> specification = createSpecification(criteria);
        return infraClasseRepository.count(specification);
    }

    /**
     * Function to convert {@link InfraClasseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InfraClasse> createSpecification(InfraClasseCriteria criteria) {
        Specification<InfraClasse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InfraClasse_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), InfraClasse_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), InfraClasse_.nomLatin));
            }
            if (criteria.getSuperOrdresId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperOrdresId(),
                            root -> root.join(InfraClasse_.superOrdres, JoinType.LEFT).get(SuperOrdre_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(InfraClasse_.synonymes, JoinType.LEFT).get(InfraClasse_.id)
                        )
                    );
            }
            if (criteria.getSousClasseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousClasseId(),
                            root -> root.join(InfraClasse_.sousClasse, JoinType.LEFT).get(SousClasse_.id)
                        )
                    );
            }
            if (criteria.getInfraClasseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraClasseId(),
                            root -> root.join(InfraClasse_.infraClasse, JoinType.LEFT).get(InfraClasse_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
