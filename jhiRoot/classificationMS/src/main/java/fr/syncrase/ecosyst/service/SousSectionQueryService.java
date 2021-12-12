package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousSection;
import fr.syncrase.ecosyst.repository.SousSectionRepository;
import fr.syncrase.ecosyst.service.criteria.SousSectionCriteria;
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
 * Service for executing complex queries for {@link SousSection} entities in the database.
 * The main input is a {@link SousSectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousSection} or a {@link Page} of {@link SousSection} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousSectionQueryService extends QueryService<SousSection> {

    private final Logger log = LoggerFactory.getLogger(SousSectionQueryService.class);

    private final SousSectionRepository sousSectionRepository;

    public SousSectionQueryService(SousSectionRepository sousSectionRepository) {
        this.sousSectionRepository = sousSectionRepository;
    }

    /**
     * Return a {@link List} of {@link SousSection} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousSection> findByCriteria(SousSectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousSection> specification = createSpecification(criteria);
        return sousSectionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousSection} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousSection> findByCriteria(SousSectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousSection> specification = createSpecification(criteria);
        return sousSectionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousSectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousSection> specification = createSpecification(criteria);
        return sousSectionRepository.count(specification);
    }

    /**
     * Function to convert {@link SousSectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousSection> createSpecification(SousSectionCriteria criteria) {
        Specification<SousSection> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousSection_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousSection_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousSection_.nomLatin));
            }
            if (criteria.getEspecesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEspecesId(), root -> root.join(SousSection_.especes, JoinType.LEFT).get(Espece_.id))
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousSection_.synonymes, JoinType.LEFT).get(SousSection_.id)
                        )
                    );
            }
            if (criteria.getSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSectionId(), root -> root.join(SousSection_.section, JoinType.LEFT).get(Section_.id))
                    );
            }
            if (criteria.getSousSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousSectionId(),
                            root -> root.join(SousSection_.sousSection, JoinType.LEFT).get(SousSection_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
