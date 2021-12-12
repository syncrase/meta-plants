package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousEspece;
import fr.syncrase.ecosyst.repository.SousEspeceRepository;
import fr.syncrase.ecosyst.service.criteria.SousEspeceCriteria;
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
 * Service for executing complex queries for {@link SousEspece} entities in the database.
 * The main input is a {@link SousEspeceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousEspece} or a {@link Page} of {@link SousEspece} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousEspeceQueryService extends QueryService<SousEspece> {

    private final Logger log = LoggerFactory.getLogger(SousEspeceQueryService.class);

    private final SousEspeceRepository sousEspeceRepository;

    public SousEspeceQueryService(SousEspeceRepository sousEspeceRepository) {
        this.sousEspeceRepository = sousEspeceRepository;
    }

    /**
     * Return a {@link List} of {@link SousEspece} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousEspece> findByCriteria(SousEspeceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousEspece> specification = createSpecification(criteria);
        return sousEspeceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousEspece} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousEspece> findByCriteria(SousEspeceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousEspece> specification = createSpecification(criteria);
        return sousEspeceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousEspeceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousEspece> specification = createSpecification(criteria);
        return sousEspeceRepository.count(specification);
    }

    /**
     * Function to convert {@link SousEspeceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousEspece> createSpecification(SousEspeceCriteria criteria) {
        Specification<SousEspece> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousEspece_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousEspece_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousEspece_.nomLatin));
            }
            if (criteria.getVarietesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVarietesId(),
                            root -> root.join(SousEspece_.varietes, JoinType.LEFT).get(Variete_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousEspece_.synonymes, JoinType.LEFT).get(SousEspece_.id)
                        )
                    );
            }
            if (criteria.getEspeceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEspeceId(), root -> root.join(SousEspece_.espece, JoinType.LEFT).get(Espece_.id))
                    );
            }
            if (criteria.getSousEspeceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousEspeceId(),
                            root -> root.join(SousEspece_.sousEspece, JoinType.LEFT).get(SousEspece_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
