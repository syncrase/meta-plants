package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousTribu;
import fr.syncrase.ecosyst.repository.SousTribuRepository;
import fr.syncrase.ecosyst.service.criteria.SousTribuCriteria;
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
 * Service for executing complex queries for {@link SousTribu} entities in the database.
 * The main input is a {@link SousTribuCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousTribu} or a {@link Page} of {@link SousTribu} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousTribuQueryService extends QueryService<SousTribu> {

    private final Logger log = LoggerFactory.getLogger(SousTribuQueryService.class);

    private final SousTribuRepository sousTribuRepository;

    public SousTribuQueryService(SousTribuRepository sousTribuRepository) {
        this.sousTribuRepository = sousTribuRepository;
    }

    /**
     * Return a {@link List} of {@link SousTribu} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousTribu> findByCriteria(SousTribuCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousTribu> specification = createSpecification(criteria);
        return sousTribuRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousTribu} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousTribu> findByCriteria(SousTribuCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousTribu> specification = createSpecification(criteria);
        return sousTribuRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousTribuCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousTribu> specification = createSpecification(criteria);
        return sousTribuRepository.count(specification);
    }

    /**
     * Function to convert {@link SousTribuCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousTribu> createSpecification(SousTribuCriteria criteria) {
        Specification<SousTribu> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousTribu_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousTribu_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousTribu_.nomLatin));
            }
            if (criteria.getGenresId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGenresId(), root -> root.join(SousTribu_.genres, JoinType.LEFT).get(Genre_.id))
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousTribu_.synonymes, JoinType.LEFT).get(SousTribu_.id)
                        )
                    );
            }
            if (criteria.getTribuId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTribuId(), root -> root.join(SousTribu_.tribu, JoinType.LEFT).get(Tribu_.id))
                    );
            }
            if (criteria.getSousTribuId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousTribuId(),
                            root -> root.join(SousTribu_.sousTribu, JoinType.LEFT).get(SousTribu_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
