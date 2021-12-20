package fr.syncrase.ecosyst.service;

import fr.syncrase.ecosyst.domain.*; // for static metamodels
import fr.syncrase.ecosyst.domain.ClassificationCronquist;
import fr.syncrase.ecosyst.repository.ClassificationCronquistRepository;
import fr.syncrase.ecosyst.service.criteria.ClassificationCronquistCriteria;
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
 * Service for executing complex queries for {@link ClassificationCronquist} entities in the database.
 * The main input is a {@link ClassificationCronquistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassificationCronquist} or a {@link Page} of {@link ClassificationCronquist} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassificationCronquistQueryService extends QueryService<ClassificationCronquist> {

    private final Logger log = LoggerFactory.getLogger(ClassificationCronquistQueryService.class);

    private final ClassificationCronquistRepository classificationCronquistRepository;

    public ClassificationCronquistQueryService(ClassificationCronquistRepository classificationCronquistRepository) {
        this.classificationCronquistRepository = classificationCronquistRepository;
    }

    /**
     * Return a {@link List} of {@link ClassificationCronquist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassificationCronquist> findByCriteria(ClassificationCronquistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassificationCronquist> specification = createSpecification(criteria);
        return classificationCronquistRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ClassificationCronquist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassificationCronquist> findByCriteria(ClassificationCronquistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassificationCronquist> specification = createSpecification(criteria);
        return classificationCronquistRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassificationCronquistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassificationCronquist> specification = createSpecification(criteria);
        return classificationCronquistRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassificationCronquistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassificationCronquist> createSpecification(ClassificationCronquistCriteria criteria) {
        Specification<ClassificationCronquist> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassificationCronquist_.id));
            }
            if (criteria.getSuperRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSuperRegne(), ClassificationCronquist_.superRegne));
            }
            if (criteria.getRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegne(), ClassificationCronquist_.regne));
            }
            if (criteria.getSousRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousRegne(), ClassificationCronquist_.sousRegne));
            }
            if (criteria.getRameau() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRameau(), ClassificationCronquist_.rameau));
            }
            if (criteria.getInfraRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfraRegne(), ClassificationCronquist_.infraRegne));
            }
            if (criteria.getSuperEmbranchement() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getSuperEmbranchement(), ClassificationCronquist_.superEmbranchement)
                    );
            }
            if (criteria.getDivision() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDivision(), ClassificationCronquist_.division));
            }
            if (criteria.getSousEmbranchement() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getSousEmbranchement(), ClassificationCronquist_.sousEmbranchement)
                    );
            }
            if (criteria.getInfraEmbranchement() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getInfraEmbranchement(), ClassificationCronquist_.infraEmbranchement)
                    );
            }
            if (criteria.getMicroEmbranchement() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getMicroEmbranchement(), ClassificationCronquist_.microEmbranchement)
                    );
            }
            if (criteria.getSuperClasse() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSuperClasse(), ClassificationCronquist_.superClasse));
            }
            if (criteria.getClasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClasse(), ClassificationCronquist_.classe));
            }
            if (criteria.getSousClasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousClasse(), ClassificationCronquist_.sousClasse));
            }
            if (criteria.getInfraClasse() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInfraClasse(), ClassificationCronquist_.infraClasse));
            }
            if (criteria.getSuperOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSuperOrdre(), ClassificationCronquist_.superOrdre));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), ClassificationCronquist_.ordre));
            }
            if (criteria.getSousOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousOrdre(), ClassificationCronquist_.sousOrdre));
            }
            if (criteria.getInfraOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfraOrdre(), ClassificationCronquist_.infraOrdre));
            }
            if (criteria.getMicroOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMicroOrdre(), ClassificationCronquist_.microOrdre));
            }
            if (criteria.getSuperFamille() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSuperFamille(), ClassificationCronquist_.superFamille));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), ClassificationCronquist_.famille));
            }
            if (criteria.getSousFamille() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSousFamille(), ClassificationCronquist_.sousFamille));
            }
            if (criteria.getTribu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTribu(), ClassificationCronquist_.tribu));
            }
            if (criteria.getSousTribu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousTribu(), ClassificationCronquist_.sousTribu));
            }
            if (criteria.getGenre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenre(), ClassificationCronquist_.genre));
            }
            if (criteria.getSousGenre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousGenre(), ClassificationCronquist_.sousGenre));
            }
            if (criteria.getSection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSection(), ClassificationCronquist_.section));
            }
            if (criteria.getSousSection() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSousSection(), ClassificationCronquist_.sousSection));
            }
            if (criteria.getEspece() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEspece(), ClassificationCronquist_.espece));
            }
            if (criteria.getSousEspece() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousEspece(), ClassificationCronquist_.sousEspece));
            }
            if (criteria.getVariete() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVariete(), ClassificationCronquist_.variete));
            }
            if (criteria.getSousVariete() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSousVariete(), ClassificationCronquist_.sousVariete));
            }
            if (criteria.getForme() != null) {
                specification = specification.and(buildStringSpecification(criteria.getForme(), ClassificationCronquist_.forme));
            }
            if (criteria.getPlanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlanteId(),
                            root -> root.join(ClassificationCronquist_.plante, JoinType.LEFT).get(Plante_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
