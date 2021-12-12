package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousClasse;
import fr.syncrase.ecosyst.repository.SousClasseRepository;
import fr.syncrase.ecosyst.service.criteria.SousClasseCriteria;
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
 * Service for executing complex queries for {@link SousClasse} entities in the database.
 * The main input is a {@link SousClasseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousClasse} or a {@link Page} of {@link SousClasse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousClasseQueryService extends QueryService<SousClasse> {

    private final Logger log = LoggerFactory.getLogger(SousClasseQueryService.class);

    private final SousClasseRepository sousClasseRepository;

    public SousClasseQueryService(SousClasseRepository sousClasseRepository) {
        this.sousClasseRepository = sousClasseRepository;
    }

    /**
     * Return a {@link List} of {@link SousClasse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousClasse> findByCriteria(SousClasseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousClasse> specification = createSpecification(criteria);
        return sousClasseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousClasse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousClasse> findByCriteria(SousClasseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousClasse> specification = createSpecification(criteria);
        return sousClasseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousClasseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousClasse> specification = createSpecification(criteria);
        return sousClasseRepository.count(specification);
    }

    /**
     * Function to convert {@link SousClasseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousClasse> createSpecification(SousClasseCriteria criteria) {
        Specification<SousClasse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousClasse_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousClasse_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousClasse_.nomLatin));
            }
            if (criteria.getInfraClassesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraClassesId(),
                            root -> root.join(SousClasse_.infraClasses, JoinType.LEFT).get(InfraClasse_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousClasse_.synonymes, JoinType.LEFT).get(SousClasse_.id)
                        )
                    );
            }
            if (criteria.getClasseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClasseId(), root -> root.join(SousClasse_.classe, JoinType.LEFT).get(Classe_.id))
                    );
            }
            if (criteria.getSousClasseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousClasseId(),
                            root -> root.join(SousClasse_.sousClasse, JoinType.LEFT).get(SousClasse_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
