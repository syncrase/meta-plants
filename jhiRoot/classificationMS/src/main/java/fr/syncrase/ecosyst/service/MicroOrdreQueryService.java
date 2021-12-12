package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.MicroOrdre;
import fr.syncrase.ecosyst.repository.MicroOrdreRepository;
import fr.syncrase.ecosyst.service.criteria.MicroOrdreCriteria;
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
 * Service for executing complex queries for {@link MicroOrdre} entities in the database.
 * The main input is a {@link MicroOrdreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MicroOrdre} or a {@link Page} of {@link MicroOrdre} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MicroOrdreQueryService extends QueryService<MicroOrdre> {

    private final Logger log = LoggerFactory.getLogger(MicroOrdreQueryService.class);

    private final MicroOrdreRepository microOrdreRepository;

    public MicroOrdreQueryService(MicroOrdreRepository microOrdreRepository) {
        this.microOrdreRepository = microOrdreRepository;
    }

    /**
     * Return a {@link List} of {@link MicroOrdre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MicroOrdre> findByCriteria(MicroOrdreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MicroOrdre> specification = createSpecification(criteria);
        return microOrdreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MicroOrdre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MicroOrdre> findByCriteria(MicroOrdreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MicroOrdre> specification = createSpecification(criteria);
        return microOrdreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MicroOrdreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MicroOrdre> specification = createSpecification(criteria);
        return microOrdreRepository.count(specification);
    }

    /**
     * Function to convert {@link MicroOrdreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MicroOrdre> createSpecification(MicroOrdreCriteria criteria) {
        Specification<MicroOrdre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MicroOrdre_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), MicroOrdre_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), MicroOrdre_.nomLatin));
            }
            if (criteria.getSuperFamillesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperFamillesId(),
                            root -> root.join(MicroOrdre_.superFamilles, JoinType.LEFT).get(SuperFamille_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(MicroOrdre_.synonymes, JoinType.LEFT).get(MicroOrdre_.id)
                        )
                    );
            }
            if (criteria.getInfraOrdreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInfraOrdreId(),
                            root -> root.join(MicroOrdre_.infraOrdre, JoinType.LEFT).get(InfraOrdre_.id)
                        )
                    );
            }
            if (criteria.getMicroOrdreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMicroOrdreId(),
                            root -> root.join(MicroOrdre_.microOrdre, JoinType.LEFT).get(MicroOrdre_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
