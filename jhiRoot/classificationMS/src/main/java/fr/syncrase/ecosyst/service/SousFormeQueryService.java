package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousForme;
import fr.syncrase.ecosyst.repository.SousFormeRepository;
import fr.syncrase.ecosyst.service.criteria.SousFormeCriteria;
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
 * Service for executing complex queries for {@link SousForme} entities in the database.
 * The main input is a {@link SousFormeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousForme} or a {@link Page} of {@link SousForme} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousFormeQueryService extends QueryService<SousForme> {

    private final Logger log = LoggerFactory.getLogger(SousFormeQueryService.class);

    private final SousFormeRepository sousFormeRepository;

    public SousFormeQueryService(SousFormeRepository sousFormeRepository) {
        this.sousFormeRepository = sousFormeRepository;
    }

    /**
     * Return a {@link List} of {@link SousForme} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousForme> findByCriteria(SousFormeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousForme> specification = createSpecification(criteria);
        return sousFormeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousForme} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousForme> findByCriteria(SousFormeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousForme> specification = createSpecification(criteria);
        return sousFormeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousFormeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousForme> specification = createSpecification(criteria);
        return sousFormeRepository.count(specification);
    }

    /**
     * Function to convert {@link SousFormeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousForme> createSpecification(SousFormeCriteria criteria) {
        Specification<SousForme> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousForme_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousForme_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousForme_.nomLatin));
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousForme_.synonymes, JoinType.LEFT).get(SousForme_.id)
                        )
                    );
            }
            if (criteria.getFormeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFormeId(), root -> root.join(SousForme_.forme, JoinType.LEFT).get(Forme_.id))
                    );
            }
            if (criteria.getSousFormeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousFormeId(),
                            root -> root.join(SousForme_.sousForme, JoinType.LEFT).get(SousForme_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
