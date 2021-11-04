package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.CycleDeVie;
import fr.syncrase.perma.repository.CycleDeVieRepository;
import fr.syncrase.perma.service.criteria.CycleDeVieCriteria;
import fr.syncrase.perma.service.dto.CycleDeVieDTO;
import fr.syncrase.perma.service.mapper.CycleDeVieMapper;
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
 * Service for executing complex queries for {@link CycleDeVie} entities in the database.
 * The main input is a {@link CycleDeVieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CycleDeVieDTO} or a {@link Page} of {@link CycleDeVieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CycleDeVieQueryService extends QueryService<CycleDeVie> {

    private final Logger log = LoggerFactory.getLogger(CycleDeVieQueryService.class);

    private final CycleDeVieRepository cycleDeVieRepository;

    private final CycleDeVieMapper cycleDeVieMapper;

    public CycleDeVieQueryService(CycleDeVieRepository cycleDeVieRepository, CycleDeVieMapper cycleDeVieMapper) {
        this.cycleDeVieRepository = cycleDeVieRepository;
        this.cycleDeVieMapper = cycleDeVieMapper;
    }

    /**
     * Return a {@link List} of {@link CycleDeVieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CycleDeVieDTO> findByCriteria(CycleDeVieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CycleDeVie> specification = createSpecification(criteria);
        return cycleDeVieMapper.toDto(cycleDeVieRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CycleDeVieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CycleDeVieDTO> findByCriteria(CycleDeVieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CycleDeVie> specification = createSpecification(criteria);
        return cycleDeVieRepository.findAll(specification, page).map(cycleDeVieMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CycleDeVieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CycleDeVie> specification = createSpecification(criteria);
        return cycleDeVieRepository.count(specification);
    }

    /**
     * Function to convert {@link CycleDeVieCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CycleDeVie> createSpecification(CycleDeVieCriteria criteria) {
        Specification<CycleDeVie> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CycleDeVie_.id));
            }
            if (criteria.getSemisId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSemisId(), root -> root.join(CycleDeVie_.semis, JoinType.LEFT).get(Semis_.id))
                    );
            }
            if (criteria.getApparitionFeuillesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApparitionFeuillesId(),
                            root -> root.join(CycleDeVie_.apparitionFeuilles, JoinType.LEFT).get(PeriodeAnnee_.id)
                        )
                    );
            }
            if (criteria.getFloraisonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFloraisonId(),
                            root -> root.join(CycleDeVie_.floraison, JoinType.LEFT).get(PeriodeAnnee_.id)
                        )
                    );
            }
            if (criteria.getRecolteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRecolteId(),
                            root -> root.join(CycleDeVie_.recolte, JoinType.LEFT).get(PeriodeAnnee_.id)
                        )
                    );
            }
            if (criteria.getCroissanceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCroissanceId(),
                            root -> root.join(CycleDeVie_.croissance, JoinType.LEFT).get(PeriodeAnnee_.id)
                        )
                    );
            }
            if (criteria.getMaturiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMaturiteId(),
                            root -> root.join(CycleDeVie_.maturite, JoinType.LEFT).get(PeriodeAnnee_.id)
                        )
                    );
            }
            if (criteria.getPlantationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlantationId(),
                            root -> root.join(CycleDeVie_.plantation, JoinType.LEFT).get(PeriodeAnnee_.id)
                        )
                    );
            }
            if (criteria.getRempotageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRempotageId(),
                            root -> root.join(CycleDeVie_.rempotage, JoinType.LEFT).get(PeriodeAnnee_.id)
                        )
                    );
            }
            if (criteria.getReproductionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReproductionId(),
                            root -> root.join(CycleDeVie_.reproduction, JoinType.LEFT).get(Reproduction_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
