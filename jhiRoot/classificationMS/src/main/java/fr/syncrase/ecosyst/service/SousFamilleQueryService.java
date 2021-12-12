package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousFamille;
import fr.syncrase.ecosyst.repository.SousFamilleRepository;
import fr.syncrase.ecosyst.service.criteria.SousFamilleCriteria;
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
 * Service for executing complex queries for {@link SousFamille} entities in the database.
 * The main input is a {@link SousFamilleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousFamille} or a {@link Page} of {@link SousFamille} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousFamilleQueryService extends QueryService<SousFamille> {

    private final Logger log = LoggerFactory.getLogger(SousFamilleQueryService.class);

    private final SousFamilleRepository sousFamilleRepository;

    public SousFamilleQueryService(SousFamilleRepository sousFamilleRepository) {
        this.sousFamilleRepository = sousFamilleRepository;
    }

    /**
     * Return a {@link List} of {@link SousFamille} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousFamille> findByCriteria(SousFamilleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousFamille> specification = createSpecification(criteria);
        return sousFamilleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousFamille} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousFamille> findByCriteria(SousFamilleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousFamille> specification = createSpecification(criteria);
        return sousFamilleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousFamilleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousFamille> specification = createSpecification(criteria);
        return sousFamilleRepository.count(specification);
    }

    /**
     * Function to convert {@link SousFamilleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousFamille> createSpecification(SousFamilleCriteria criteria) {
        Specification<SousFamille> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousFamille_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousFamille_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousFamille_.nomLatin));
            }
            if (criteria.getTribusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTribusId(), root -> root.join(SousFamille_.tribuses, JoinType.LEFT).get(Tribu_.id))
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousFamille_.synonymes, JoinType.LEFT).get(SousFamille_.id)
                        )
                    );
            }
            if (criteria.getFamilleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFamilleId(), root -> root.join(SousFamille_.famille, JoinType.LEFT).get(Famille_.id))
                    );
            }
            if (criteria.getSousFamilleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousFamilleId(),
                            root -> root.join(SousFamille_.sousFamille, JoinType.LEFT).get(SousFamille_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
