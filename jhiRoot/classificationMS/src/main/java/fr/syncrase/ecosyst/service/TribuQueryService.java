package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Tribu;
import fr.syncrase.ecosyst.repository.TribuRepository;
import fr.syncrase.ecosyst.service.criteria.TribuCriteria;
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
 * Service for executing complex queries for {@link Tribu} entities in the database.
 * The main input is a {@link TribuCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Tribu} or a {@link Page} of {@link Tribu} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TribuQueryService extends QueryService<Tribu> {

    private final Logger log = LoggerFactory.getLogger(TribuQueryService.class);

    private final TribuRepository tribuRepository;

    public TribuQueryService(TribuRepository tribuRepository) {
        this.tribuRepository = tribuRepository;
    }

    /**
     * Return a {@link List} of {@link Tribu} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Tribu> findByCriteria(TribuCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tribu> specification = createSpecification(criteria);
        return tribuRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Tribu} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Tribu> findByCriteria(TribuCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tribu> specification = createSpecification(criteria);
        return tribuRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TribuCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tribu> specification = createSpecification(criteria);
        return tribuRepository.count(specification);
    }

    /**
     * Function to convert {@link TribuCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tribu> createSpecification(TribuCriteria criteria) {
        Specification<Tribu> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tribu_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Tribu_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Tribu_.nomLatin));
            }
            if (criteria.getSousTribusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousTribusId(),
                            root -> root.join(Tribu_.sousTribuses, JoinType.LEFT).get(SousTribu_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSynonymesId(), root -> root.join(Tribu_.synonymes, JoinType.LEFT).get(Tribu_.id))
                    );
            }
            if (criteria.getSousFamilleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousFamilleId(),
                            root -> root.join(Tribu_.sousFamille, JoinType.LEFT).get(SousFamille_.id)
                        )
                    );
            }
            if (criteria.getTribuId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTribuId(), root -> root.join(Tribu_.tribu, JoinType.LEFT).get(Tribu_.id))
                    );
            }
        }
        return specification;
    }
}
