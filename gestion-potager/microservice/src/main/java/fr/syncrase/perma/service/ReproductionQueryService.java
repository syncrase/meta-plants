package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Reproduction;
import fr.syncrase.perma.repository.ReproductionRepository;
import fr.syncrase.perma.service.criteria.ReproductionCriteria;
import fr.syncrase.perma.service.dto.ReproductionDTO;
import fr.syncrase.perma.service.mapper.ReproductionMapper;
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
 * Service for executing complex queries for {@link Reproduction} entities in the database.
 * The main input is a {@link ReproductionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReproductionDTO} or a {@link Page} of {@link ReproductionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReproductionQueryService extends QueryService<Reproduction> {

    private final Logger log = LoggerFactory.getLogger(ReproductionQueryService.class);

    private final ReproductionRepository reproductionRepository;

    private final ReproductionMapper reproductionMapper;

    public ReproductionQueryService(ReproductionRepository reproductionRepository, ReproductionMapper reproductionMapper) {
        this.reproductionRepository = reproductionRepository;
        this.reproductionMapper = reproductionMapper;
    }

    /**
     * Return a {@link List} of {@link ReproductionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReproductionDTO> findByCriteria(ReproductionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reproduction> specification = createSpecification(criteria);
        return reproductionMapper.toDto(reproductionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReproductionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReproductionDTO> findByCriteria(ReproductionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reproduction> specification = createSpecification(criteria);
        return reproductionRepository.findAll(specification, page).map(reproductionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReproductionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reproduction> specification = createSpecification(criteria);
        return reproductionRepository.count(specification);
    }

    /**
     * Function to convert {@link ReproductionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reproduction> createSpecification(ReproductionCriteria criteria) {
        Specification<Reproduction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Reproduction_.id));
            }
            if (criteria.getVitesse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVitesse(), Reproduction_.vitesse));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Reproduction_.type));
            }
            if (criteria.getCycleDeVieId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCycleDeVieId(),
                            root -> root.join(Reproduction_.cycleDeVies, JoinType.LEFT).get(CycleDeVie_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
