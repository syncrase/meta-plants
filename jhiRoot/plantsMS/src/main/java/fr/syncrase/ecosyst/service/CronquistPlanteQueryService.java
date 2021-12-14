package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.CronquistPlante;
import fr.syncrase.ecosyst.repository.CronquistPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.CronquistPlanteCriteria;
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
 * Service for executing complex queries for {@link CronquistPlante} entities in the database.
 * The main input is a {@link CronquistPlanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CronquistPlante} or a {@link Page} of {@link CronquistPlante} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CronquistPlanteQueryService extends QueryService<CronquistPlante> {

    private final Logger log = LoggerFactory.getLogger(CronquistPlanteQueryService.class);

    private final CronquistPlanteRepository cronquistPlanteRepository;

    public CronquistPlanteQueryService(CronquistPlanteRepository cronquistPlanteRepository) {
        this.cronquistPlanteRepository = cronquistPlanteRepository;
    }

    /**
     * Return a {@link List} of {@link CronquistPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CronquistPlante> findByCriteria(CronquistPlanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CronquistPlante> specification = createSpecification(criteria);
        return cronquistPlanteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CronquistPlante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CronquistPlante> findByCriteria(CronquistPlanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CronquistPlante> specification = createSpecification(criteria);
        return cronquistPlanteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CronquistPlanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CronquistPlante> specification = createSpecification(criteria);
        return cronquistPlanteRepository.count(specification);
    }

    /**
     * Function to convert {@link CronquistPlanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CronquistPlante> createSpecification(CronquistPlanteCriteria criteria) {
        Specification<CronquistPlante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CronquistPlante_.id));
            }
            if (criteria.getSuperRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSuperRegne(), CronquistPlante_.superRegne));
            }
            if (criteria.getRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegne(), CronquistPlante_.regne));
            }
            if (criteria.getSousRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousRegne(), CronquistPlante_.sousRegne));
            }
            if (criteria.getRameau() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRameau(), CronquistPlante_.rameau));
            }
            if (criteria.getInfraRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfraRegne(), CronquistPlante_.infraRegne));
            }
            if (criteria.getSuperDivision() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSuperDivision(), CronquistPlante_.superDivision));
            }
            if (criteria.getDivision() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDivision(), CronquistPlante_.division));
            }
            if (criteria.getSousDivision() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousDivision(), CronquistPlante_.sousDivision));
            }
            if (criteria.getInfraEmbranchement() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInfraEmbranchement(), CronquistPlante_.infraEmbranchement));
            }
            if (criteria.getMicroEmbranchement() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMicroEmbranchement(), CronquistPlante_.microEmbranchement));
            }
            if (criteria.getSuperClasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSuperClasse(), CronquistPlante_.superClasse));
            }
            if (criteria.getClasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClasse(), CronquistPlante_.classe));
            }
            if (criteria.getSousClasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousClasse(), CronquistPlante_.sousClasse));
            }
            if (criteria.getInfraClasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfraClasse(), CronquistPlante_.infraClasse));
            }
            if (criteria.getSuperOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSuperOrdre(), CronquistPlante_.superOrdre));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), CronquistPlante_.ordre));
            }
            if (criteria.getSousOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousOrdre(), CronquistPlante_.sousOrdre));
            }
            if (criteria.getInfraOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfraOrdre(), CronquistPlante_.infraOrdre));
            }
            if (criteria.getMicroOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMicroOrdre(), CronquistPlante_.microOrdre));
            }
            if (criteria.getSuperFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSuperFamille(), CronquistPlante_.superFamille));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), CronquistPlante_.famille));
            }
            if (criteria.getSousFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousFamille(), CronquistPlante_.sousFamille));
            }
            if (criteria.getTribu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTribu(), CronquistPlante_.tribu));
            }
            if (criteria.getSousTribu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousTribu(), CronquistPlante_.sousTribu));
            }
            if (criteria.getGenre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenre(), CronquistPlante_.genre));
            }
            if (criteria.getSousGenre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousGenre(), CronquistPlante_.sousGenre));
            }
            if (criteria.getSection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSection(), CronquistPlante_.section));
            }
            if (criteria.getSousSection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousSection(), CronquistPlante_.sousSection));
            }
            if (criteria.getEspece() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEspece(), CronquistPlante_.espece));
            }
            if (criteria.getSousEspece() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousEspece(), CronquistPlante_.sousEspece));
            }
            if (criteria.getVariete() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVariete(), CronquistPlante_.variete));
            }
            if (criteria.getSousVariete() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousVariete(), CronquistPlante_.sousVariete));
            }
            if (criteria.getForme() != null) {
                specification = specification.and(buildStringSpecification(criteria.getForme(), CronquistPlante_.forme));
            }
        }
        return specification;
    }
}
