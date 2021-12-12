package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Forme;
import fr.syncrase.ecosyst.repository.FormeRepository;
import fr.syncrase.ecosyst.service.criteria.FormeCriteria;
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
 * Service for executing complex queries for {@link Forme} entities in the database.
 * The main input is a {@link FormeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Forme} or a {@link Page} of {@link Forme} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormeQueryService extends QueryService<Forme> {

    private final Logger log = LoggerFactory.getLogger(FormeQueryService.class);

    private final FormeRepository formeRepository;

    public FormeQueryService(FormeRepository formeRepository) {
        this.formeRepository = formeRepository;
    }

    /**
     * Return a {@link List} of {@link Forme} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Forme> findByCriteria(FormeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Forme> specification = createSpecification(criteria);
        return formeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Forme} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Forme> findByCriteria(FormeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Forme> specification = createSpecification(criteria);
        return formeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Forme> specification = createSpecification(criteria);
        return formeRepository.count(specification);
    }

    /**
     * Function to convert {@link FormeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Forme> createSpecification(FormeCriteria criteria) {
        Specification<Forme> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Forme_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Forme_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Forme_.nomLatin));
            }
            if (criteria.getSousFormesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousFormesId(),
                            root -> root.join(Forme_.sousFormes, JoinType.LEFT).get(SousForme_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSynonymesId(), root -> root.join(Forme_.synonymes, JoinType.LEFT).get(Forme_.id))
                    );
            }
            if (criteria.getSousVarieteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousVarieteId(),
                            root -> root.join(Forme_.sousVariete, JoinType.LEFT).get(SousVariete_.id)
                        )
                    );
            }
            if (criteria.getFormeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFormeId(), root -> root.join(Forme_.forme, JoinType.LEFT).get(Forme_.id))
                    );
            }
        }
        return specification;
    }
}
