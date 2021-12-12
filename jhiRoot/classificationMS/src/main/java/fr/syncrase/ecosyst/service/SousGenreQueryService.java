package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.SousGenre;
import fr.syncrase.ecosyst.repository.SousGenreRepository;
import fr.syncrase.ecosyst.service.criteria.SousGenreCriteria;
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
 * Service for executing complex queries for {@link SousGenre} entities in the database.
 * The main input is a {@link SousGenreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SousGenre} or a {@link Page} of {@link SousGenre} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SousGenreQueryService extends QueryService<SousGenre> {

    private final Logger log = LoggerFactory.getLogger(SousGenreQueryService.class);

    private final SousGenreRepository sousGenreRepository;

    public SousGenreQueryService(SousGenreRepository sousGenreRepository) {
        this.sousGenreRepository = sousGenreRepository;
    }

    /**
     * Return a {@link List} of {@link SousGenre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SousGenre> findByCriteria(SousGenreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SousGenre> specification = createSpecification(criteria);
        return sousGenreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SousGenre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SousGenre> findByCriteria(SousGenreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SousGenre> specification = createSpecification(criteria);
        return sousGenreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SousGenreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SousGenre> specification = createSpecification(criteria);
        return sousGenreRepository.count(specification);
    }

    /**
     * Function to convert {@link SousGenreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SousGenre> createSpecification(SousGenreCriteria criteria) {
        Specification<SousGenre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SousGenre_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), SousGenre_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), SousGenre_.nomLatin));
            }
            if (criteria.getSectionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSectionsId(), root -> root.join(SousGenre_.sections, JoinType.LEFT).get(Section_.id))
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSynonymesId(),
                            root -> root.join(SousGenre_.synonymes, JoinType.LEFT).get(SousGenre_.id)
                        )
                    );
            }
            if (criteria.getGenreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGenreId(), root -> root.join(SousGenre_.genre, JoinType.LEFT).get(Genre_.id))
                    );
            }
            if (criteria.getSousGenreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousGenreId(),
                            root -> root.join(SousGenre_.sousGenre, JoinType.LEFT).get(SousGenre_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
