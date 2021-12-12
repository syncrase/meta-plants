package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Classe;
import fr.syncrase.ecosyst.repository.ClasseRepository;
import fr.syncrase.ecosyst.service.criteria.ClasseCriteria;
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
 * Service for executing complex queries for {@link Classe} entities in the database.
 * The main input is a {@link ClasseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Classe} or a {@link Page} of {@link Classe} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClasseQueryService extends QueryService<Classe> {

    private final Logger log = LoggerFactory.getLogger(ClasseQueryService.class);

    private final ClasseRepository classeRepository;

    public ClasseQueryService(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    /**
     * Return a {@link List} of {@link Classe} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Classe> findByCriteria(ClasseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Classe> specification = createSpecification(criteria);
        return classeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Classe} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Classe> findByCriteria(ClasseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Classe> specification = createSpecification(criteria);
        return classeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClasseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Classe> specification = createSpecification(criteria);
        return classeRepository.count(specification);
    }

    /**
     * Function to convert {@link ClasseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Classe> createSpecification(ClasseCriteria criteria) {
        Specification<Classe> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Classe_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Classe_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Classe_.nomLatin));
            }
            if (criteria.getSousClassesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousClassesId(),
                            root -> root.join(Classe_.sousClasses, JoinType.LEFT).get(SousClasse_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSynonymesId(), root -> root.join(Classe_.synonymes, JoinType.LEFT).get(Classe_.id))
                    );
            }
            if (criteria.getSuperClasseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperClasseId(),
                            root -> root.join(Classe_.superClasse, JoinType.LEFT).get(SuperClasse_.id)
                        )
                    );
            }
            if (criteria.getClasseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClasseId(), root -> root.join(Classe_.classe, JoinType.LEFT).get(Classe_.id))
                    );
            }
        }
        return specification;
    }
}
