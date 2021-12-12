package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.MicroEmbranchement;
import fr.syncrase.ecosyst.repository.MicroEmbranchementRepository;
import fr.syncrase.ecosyst.service.criteria.MicroEmbranchementCriteria;
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
 * Service for executing complex queries for {@link MicroEmbranchement} entities in the database.
 * The main input is a {@link MicroEmbranchementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MicroEmbranchement} or a {@link Page} of {@link MicroEmbranchement} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MicroEmbranchementQueryService extends QueryService<MicroEmbranchement> {

    private final Logger log = LoggerFactory.getLogger(MicroEmbranchementQueryService.class);

    private final MicroEmbranchementRepository microEmbranchementRepository;

    public MicroEmbranchementQueryService(MicroEmbranchementRepository microEmbranchementRepository) {
        this.microEmbranchementRepository = microEmbranchementRepository;
    }

    /**
     * Return a {@link List} of {@link MicroEmbranchement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MicroEmbranchement> findByCriteria(MicroEmbranchementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MicroEmbranchement> specification = createSpecification(criteria);
        return microEmbranchementRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MicroEmbranchement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MicroEmbranchement> findByCriteria(MicroEmbranchementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MicroEmbranchement> specification = createSpecification(criteria);
        return microEmbranchementRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MicroEmbranchementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MicroEmbranchement> specification = createSpecification(criteria);
        return microEmbranchementRepository.count(specification);
    }

    /**
     * Function to convert {@link MicroEmbranchementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MicroEmbranchement> createSpecification(MicroEmbranchementCriteria criteria) {
        Specification<MicroEmbranchement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MicroEmbranchement_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), MicroEmbranchement_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), MicroEmbranchement_.nomLatin));
            }
            if (criteria.getSuperClassesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperClassesId(),
                            root -> root.join(MicroEmbranchement_.superClasses, JoinType.LEFT).get(SuperClasse_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(MicroEmbranchement_.synonymes, JoinType.LEFT).get(MicroEmbranchement_.id)
                        )
                    );
            }
            if (criteria.getInfraEmbranchementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraEmbranchementId(),
                            root -> root.join(MicroEmbranchement_.infraEmbranchement, JoinType.LEFT).get(InfraEmbranchement_.id)
                        )
                    );
            }
            if (criteria.getMicroEmbranchementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMicroEmbranchementId(),
                            root -> root.join(MicroEmbranchement_.microEmbranchement, JoinType.LEFT).get(MicroEmbranchement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
