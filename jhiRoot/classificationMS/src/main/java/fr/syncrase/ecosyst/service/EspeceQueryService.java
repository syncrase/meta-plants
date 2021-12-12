package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Espece;
import fr.syncrase.ecosyst.repository.EspeceRepository;
import fr.syncrase.ecosyst.service.criteria.EspeceCriteria;
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
 * Service for executing complex queries for {@link Espece} entities in the database.
 * The main input is a {@link EspeceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Espece} or a {@link Page} of {@link Espece} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EspeceQueryService extends QueryService<Espece> {

    private final Logger log = LoggerFactory.getLogger(EspeceQueryService.class);

    private final EspeceRepository especeRepository;

    public EspeceQueryService(EspeceRepository especeRepository) {
        this.especeRepository = especeRepository;
    }

    /**
     * Return a {@link List} of {@link Espece} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Espece> findByCriteria(EspeceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Espece> specification = createSpecification(criteria);
        return especeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Espece} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Espece> findByCriteria(EspeceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Espece> specification = createSpecification(criteria);
        return especeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EspeceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Espece> specification = createSpecification(criteria);
        return especeRepository.count(specification);
    }

    /**
     * Function to convert {@link EspeceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Espece> createSpecification(EspeceCriteria criteria) {
        Specification<Espece> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Espece_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Espece_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Espece_.nomLatin));
            }
            if (criteria.getSousEspecesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousEspecesId(),
                            root -> root.join(Espece_.sousEspeces, JoinType.LEFT).get(SousEspece_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSynonymesId(), root -> root.join(Espece_.synonymes, JoinType.LEFT).get(Espece_.id))
                    );
            }
            if (criteria.getSousSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousSectionId(),
                            root -> root.join(Espece_.sousSection, JoinType.LEFT).get(SousSection_.id)
                        )
                    );
            }
            if (criteria.getEspeceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEspeceId(), root -> root.join(Espece_.espece, JoinType.LEFT).get(Espece_.id))
                    );
            }
        }
        return specification;
    }
}
