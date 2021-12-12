package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Famille;
import fr.syncrase.ecosyst.repository.FamilleRepository;
import fr.syncrase.ecosyst.service.criteria.FamilleCriteria;
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
 * Service for executing complex queries for {@link Famille} entities in the database.
 * The main input is a {@link FamilleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Famille} or a {@link Page} of {@link Famille} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FamilleQueryService extends QueryService<Famille> {

    private final Logger log = LoggerFactory.getLogger(FamilleQueryService.class);

    private final FamilleRepository familleRepository;

    public FamilleQueryService(FamilleRepository familleRepository) {
        this.familleRepository = familleRepository;
    }

    /**
     * Return a {@link List} of {@link Famille} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Famille> findByCriteria(FamilleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Famille> specification = createSpecification(criteria);
        return familleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Famille} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Famille> findByCriteria(FamilleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Famille> specification = createSpecification(criteria);
        return familleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FamilleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Famille> specification = createSpecification(criteria);
        return familleRepository.count(specification);
    }

    /**
     * Function to convert {@link FamilleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Famille> createSpecification(FamilleCriteria criteria) {
        Specification<Famille> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Famille_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Famille_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Famille_.nomLatin));
            }
            if (criteria.getSousFamillesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousFamillesId(),
                            root -> root.join(Famille_.sousFamilles, JoinType.LEFT).get(SousFamille_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSynonymesId(), root -> root.join(Famille_.synonymes, JoinType.LEFT).get(Famille_.id))
                    );
            }
            if (criteria.getSuperFamilleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperFamilleId(),
                            root -> root.join(Famille_.superFamille, JoinType.LEFT).get(SuperFamille_.id)
                        )
                    );
            }
            if (criteria.getFamilleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFamilleId(), root -> root.join(Famille_.famille, JoinType.LEFT).get(Famille_.id))
                    );
            }
        }
        return specification;
    }
}
