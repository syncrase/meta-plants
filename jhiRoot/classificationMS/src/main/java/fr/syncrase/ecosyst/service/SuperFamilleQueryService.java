package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SuperFamille;
import fr.syncrase.ecosyst.repository.SuperFamilleRepository;
import fr.syncrase.ecosyst.service.criteria.SuperFamilleCriteria;
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
 * Service for executing complex queries for {@link SuperFamille} entities in the database.
 * The main input is a {@link SuperFamilleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SuperFamille} or a {@link Page} of {@link SuperFamille} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuperFamilleQueryService extends QueryService<SuperFamille> {

    private final Logger log = LoggerFactory.getLogger(SuperFamilleQueryService.class);

    private final SuperFamilleRepository superFamilleRepository;

    public SuperFamilleQueryService(SuperFamilleRepository superFamilleRepository) {
        this.superFamilleRepository = superFamilleRepository;
    }

    /**
     * Return a {@link List} of {@link SuperFamille} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SuperFamille> findByCriteria(SuperFamilleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SuperFamille> specification = createSpecification(criteria);
        return superFamilleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SuperFamille} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SuperFamille> findByCriteria(SuperFamilleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SuperFamille> specification = createSpecification(criteria);
        return superFamilleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuperFamilleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SuperFamille> specification = createSpecification(criteria);
        return superFamilleRepository.count(specification);
    }

    /**
     * Function to convert {@link SuperFamilleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SuperFamille> createSpecification(SuperFamilleCriteria criteria) {
        Specification<SuperFamille> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SuperFamille_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SuperFamille_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SuperFamille_.nomLatin));
            }
            if (criteria.getFamillesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFamillesId(),
                            root -> root.join(SuperFamille_.familles, JoinType.LEFT).get(Famille_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SuperFamille_.synonymes, JoinType.LEFT).get(SuperFamille_.id)
                        )
                    );
            }
            if (criteria.getMicroOrdreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMicroOrdreId(),
                            root -> root.join(SuperFamille_.microOrdre, JoinType.LEFT).get(MicroOrdre_.id)
                        )
                    );
            }
            if (criteria.getSuperFamilleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperFamilleId(),
                            root -> root.join(SuperFamille_.superFamille, JoinType.LEFT).get(SuperFamille_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
