package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SuperClasse;
import fr.syncrase.ecosyst.repository.SuperClasseRepository;
import fr.syncrase.ecosyst.service.criteria.SuperClasseCriteria;
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
 * Service for executing complex queries for {@link SuperClasse} entities in the database.
 * The main input is a {@link SuperClasseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SuperClasse} or a {@link Page} of {@link SuperClasse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuperClasseQueryService extends QueryService<SuperClasse> {

    private final Logger log = LoggerFactory.getLogger(SuperClasseQueryService.class);

    private final SuperClasseRepository superClasseRepository;

    public SuperClasseQueryService(SuperClasseRepository superClasseRepository) {
        this.superClasseRepository = superClasseRepository;
    }

    /**
     * Return a {@link List} of {@link SuperClasse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SuperClasse> findByCriteria(SuperClasseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SuperClasse> specification = createSpecification(criteria);
        return superClasseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SuperClasse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SuperClasse> findByCriteria(SuperClasseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SuperClasse> specification = createSpecification(criteria);
        return superClasseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuperClasseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SuperClasse> specification = createSpecification(criteria);
        return superClasseRepository.count(specification);
    }

    /**
     * Function to convert {@link SuperClasseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SuperClasse> createSpecification(SuperClasseCriteria criteria) {
        Specification<SuperClasse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SuperClasse_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SuperClasse_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SuperClasse_.nomLatin));
            }
            if (criteria.getClassesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClassesId(), root -> root.join(SuperClasse_.classes, JoinType.LEFT).get(Classe_.id))
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SuperClasse_.synonymes, JoinType.LEFT).get(SuperClasse_.id)
                        )
                    );
            }
            if (criteria.getMicroEmbranchementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMicroEmbranchementId(),
                            root -> root.join(SuperClasse_.microEmbranchement, JoinType.LEFT).get(MicroEmbranchement_.id)
                        )
                    );
            }
            if (criteria.getSuperClasseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperClasseId(),
                            root -> root.join(SuperClasse_.superClasse, JoinType.LEFT).get(SuperClasse_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
