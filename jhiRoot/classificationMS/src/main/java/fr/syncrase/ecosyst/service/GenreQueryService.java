package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.Genre;
import fr.syncrase.ecosyst.repository.GenreRepository;
import fr.syncrase.ecosyst.service.criteria.GenreCriteria;
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
 * Service for executing complex queries for {@link Genre} entities in the database.
 * The main input is a {@link GenreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Genre} or a {@link Page} of {@link Genre} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenreQueryService extends QueryService<Genre> {

    private final Logger log = LoggerFactory.getLogger(GenreQueryService.class);

    private final GenreRepository genreRepository;

    public GenreQueryService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Return a {@link List} of {@link Genre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Genre> findByCriteria(GenreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Genre> specification = createSpecification(criteria);
        return genreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Genre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Genre> findByCriteria(GenreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Genre> specification = createSpecification(criteria);
        return genreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Genre> specification = createSpecification(criteria);
        return genreRepository.count(specification);
    }

    /**
     * Function to convert {@link GenreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Genre> createSpecification(GenreCriteria criteria) {
        Specification<Genre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Genre_.id));
            }
            if (criteria.getNomFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomFr(), Genre_.nomFr));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Genre_.nomLatin));
            }
            if (criteria.getSousGenresId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSousGenresId(),
                            root -> root.join(Genre_.sousGenres, JoinType.LEFT).get(SousGenre_.id)
                        )
                    );
            }
            if (criteria.getSynonymesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSynonymesId(), root -> root.join(Genre_.synonymes, JoinType.LEFT).get(Genre_.id))
                    );
            }
            if (criteria.getSousTribuId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSousTribuId(), root -> root.join(Genre_.sousTribu, JoinType.LEFT).get(SousTribu_.id))
                    );
            }
            if (criteria.getGenreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGenreId(), root -> root.join(Genre_.genre, JoinType.LEFT).get(Genre_.id))
                    );
            }
        }
        return specification;
    }
}
