package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.PlanteRepository;
import fr.syncrase.perma.service.criteria.PlanteCriteria;
import fr.syncrase.perma.service.dto.PlanteDTO;
import fr.syncrase.perma.service.mapper.PlanteMapper;
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
 * Service for executing complex queries for {@link Plante} entities in the database.
 * The main input is a {@link PlanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanteDTO} or a {@link Page} of {@link PlanteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanteQueryService extends QueryService<Plante> {

    private final Logger log = LoggerFactory.getLogger(PlanteQueryService.class);

    private final PlanteRepository planteRepository;

    private final PlanteMapper planteMapper;

    public PlanteQueryService(PlanteRepository planteRepository, PlanteMapper planteMapper) {
        this.planteRepository = planteRepository;
        this.planteMapper = planteMapper;
    }

    /**
     * Return a {@link List} of {@link PlanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanteDTO> findByCriteria(PlanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Plante> specification = createSpecification(criteria);
        return planteMapper.toDto(planteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanteDTO> findByCriteria(PlanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Plante> specification = createSpecification(criteria);
        return planteRepository.findAll(specification, page).map(planteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Plante> specification = createSpecification(criteria);
        return planteRepository.count(specification);
    }

    /**
     * Function to convert {@link PlanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Plante> createSpecification(PlanteCriteria criteria) {
        Specification<Plante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Plante_.id));
            }
            if (criteria.getNomLatin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLatin(), Plante_.nomLatin));
            }
            if (criteria.getEntretien() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEntretien(), Plante_.entretien));
            }
            if (criteria.getHistoire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHistoire(), Plante_.histoire));
            }
            if (criteria.getVitesseCroissance() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVitesseCroissance(), Plante_.vitesseCroissance));
            }
            if (criteria.getExposition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExposition(), Plante_.exposition));
            }
            if (criteria.getCycleDeVieId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCycleDeVieId(),
                            root -> root.join(Plante_.cycleDeVie, JoinType.LEFT).get(CycleDeVie_.id)
                        )
                    );
            }
            if (criteria.getClassificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassificationId(),
                            root -> root.join(Plante_.classification, JoinType.LEFT).get(Classification_.id)
                        )
                    );
            }
            if (criteria.getConfusionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConfusionsId(),
                            root -> root.join(Plante_.confusions, JoinType.LEFT).get(Ressemblance_.id)
                        )
                    );
            }
            if (criteria.getEnsoleillementsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnsoleillementsId(),
                            root -> root.join(Plante_.ensoleillements, JoinType.LEFT).get(Ensoleillement_.id)
                        )
                    );
            }
            if (criteria.getSolsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSolsId(), root -> root.join(Plante_.sols, JoinType.LEFT).get(Sol_.id))
                    );
            }
            if (criteria.getNomsVernaculairesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNomsVernaculairesId(),
                            root -> root.join(Plante_.nomsVernaculaires, JoinType.LEFT).get(NomVernaculaire_.id)
                        )
                    );
            }
            if (criteria.getTemperatureId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemperatureId(),
                            root -> root.join(Plante_.temperature, JoinType.LEFT).get(Temperature_.id)
                        )
                    );
            }
            if (criteria.getRacineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRacineId(), root -> root.join(Plante_.racine, JoinType.LEFT).get(Racine_.id))
                    );
            }
            if (criteria.getStrateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStrateId(), root -> root.join(Plante_.strate, JoinType.LEFT).get(Strate_.id))
                    );
            }
            if (criteria.getFeuillageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFeuillageId(),
                            root -> root.join(Plante_.feuillage, JoinType.LEFT).get(Feuillage_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
