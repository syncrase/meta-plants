package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Racine;
import fr.syncrase.perma.repository.RacineRepository;
import fr.syncrase.perma.service.criteria.RacineCriteria;
import fr.syncrase.perma.service.dto.RacineDTO;
import fr.syncrase.perma.service.mapper.RacineMapper;
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
 * Service for executing complex queries for {@link Racine} entities in the database.
 * The main input is a {@link RacineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RacineDTO} or a {@link Page} of {@link RacineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RacineQueryService extends QueryService<Racine> {

    private final Logger log = LoggerFactory.getLogger(RacineQueryService.class);

    private final RacineRepository racineRepository;

    private final RacineMapper racineMapper;

    public RacineQueryService(RacineRepository racineRepository, RacineMapper racineMapper) {
        this.racineRepository = racineRepository;
        this.racineMapper = racineMapper;
    }

    /**
     * Return a {@link List} of {@link RacineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RacineDTO> findByCriteria(RacineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Racine> specification = createSpecification(criteria);
        return racineMapper.toDto(racineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RacineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RacineDTO> findByCriteria(RacineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Racine> specification = createSpecification(criteria);
        return racineRepository.findAll(specification, page).map(racineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RacineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Racine> specification = createSpecification(criteria);
        return racineRepository.count(specification);
    }

    /**
     * Function to convert {@link RacineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Racine> createSpecification(RacineCriteria criteria) {
        Specification<Racine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Racine_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Racine_.type));
            }
            if (criteria.getPlanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPlanteId(), root -> root.join(Racine_.plantes, JoinType.LEFT).get(Plante_.id))
                    );
            }
        }
        return specification;
    }
}
