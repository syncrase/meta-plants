package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Exposition;
import fr.syncrase.perma.repository.ExpositionRepository;
import fr.syncrase.perma.service.criteria.ExpositionCriteria;
import fr.syncrase.perma.service.dto.ExpositionDTO;
import fr.syncrase.perma.service.mapper.ExpositionMapper;
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
 * Service for executing complex queries for {@link Exposition} entities in the database.
 * The main input is a {@link ExpositionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExpositionDTO} or a {@link Page} of {@link ExpositionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpositionQueryService extends QueryService<Exposition> {

    private final Logger log = LoggerFactory.getLogger(ExpositionQueryService.class);

    private final ExpositionRepository expositionRepository;

    private final ExpositionMapper expositionMapper;

    public ExpositionQueryService(ExpositionRepository expositionRepository, ExpositionMapper expositionMapper) {
        this.expositionRepository = expositionRepository;
        this.expositionMapper = expositionMapper;
    }

    /**
     * Return a {@link List} of {@link ExpositionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExpositionDTO> findByCriteria(ExpositionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Exposition> specification = createSpecification(criteria);
        return expositionMapper.toDto(expositionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExpositionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpositionDTO> findByCriteria(ExpositionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Exposition> specification = createSpecification(criteria);
        return expositionRepository.findAll(specification, page).map(expositionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpositionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Exposition> specification = createSpecification(criteria);
        return expositionRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpositionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Exposition> createSpecification(ExpositionCriteria criteria) {
        Specification<Exposition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Exposition_.id));
            }
            if (criteria.getValeur() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValeur(), Exposition_.valeur));
            }
            if (criteria.getEnsoleilement() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnsoleilement(), Exposition_.ensoleilement));
            }
            if (criteria.getPlanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPlanteId(), root -> root.join(Exposition_.plante, JoinType.LEFT).get(Plante_.id))
                    );
            }
        }
        return specification;
    }
}
