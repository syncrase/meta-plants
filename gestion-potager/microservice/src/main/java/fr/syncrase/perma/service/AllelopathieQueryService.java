package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Allelopathie;
import fr.syncrase.perma.repository.AllelopathieRepository;
import fr.syncrase.perma.service.criteria.AllelopathieCriteria;
import fr.syncrase.perma.service.dto.AllelopathieDTO;
import fr.syncrase.perma.service.mapper.AllelopathieMapper;
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
 * Service for executing complex queries for {@link Allelopathie} entities in the database.
 * The main input is a {@link AllelopathieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AllelopathieDTO} or a {@link Page} of {@link AllelopathieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AllelopathieQueryService extends QueryService<Allelopathie> {

    private final Logger log = LoggerFactory.getLogger(AllelopathieQueryService.class);

    private final AllelopathieRepository allelopathieRepository;

    private final AllelopathieMapper allelopathieMapper;

    public AllelopathieQueryService(AllelopathieRepository allelopathieRepository, AllelopathieMapper allelopathieMapper) {
        this.allelopathieRepository = allelopathieRepository;
        this.allelopathieMapper = allelopathieMapper;
    }

    /**
     * Return a {@link List} of {@link AllelopathieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AllelopathieDTO> findByCriteria(AllelopathieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Allelopathie> specification = createSpecification(criteria);
        return allelopathieMapper.toDto(allelopathieRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AllelopathieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AllelopathieDTO> findByCriteria(AllelopathieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Allelopathie> specification = createSpecification(criteria);
        return allelopathieRepository.findAll(specification, page).map(allelopathieMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AllelopathieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Allelopathie> specification = createSpecification(criteria);
        return allelopathieRepository.count(specification);
    }

    /**
     * Function to convert {@link AllelopathieCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Allelopathie> createSpecification(AllelopathieCriteria criteria) {
        Specification<Allelopathie> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Allelopathie_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Allelopathie_.type));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Allelopathie_.description));
            }
            if (criteria.getCibleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCibleId(), root -> root.join(Allelopathie_.cible, JoinType.LEFT).get(Plante_.id))
                    );
            }
            if (criteria.getOrigineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrigineId(), root -> root.join(Allelopathie_.origine, JoinType.LEFT).get(Plante_.id))
                    );
            }
            if (criteria.getInteractionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInteractionId(),
                            root -> root.join(Allelopathie_.interaction, JoinType.LEFT).get(Plante_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
