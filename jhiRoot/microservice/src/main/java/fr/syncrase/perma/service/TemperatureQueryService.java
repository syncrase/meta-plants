package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Temperature;
import fr.syncrase.perma.repository.TemperatureRepository;
import fr.syncrase.perma.service.criteria.TemperatureCriteria;
import fr.syncrase.perma.service.dto.TemperatureDTO;
import fr.syncrase.perma.service.mapper.TemperatureMapper;
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
 * Service for executing complex queries for {@link Temperature} entities in the database.
 * The main input is a {@link TemperatureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TemperatureDTO} or a {@link Page} of {@link TemperatureDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemperatureQueryService extends QueryService<Temperature> {

    private final Logger log = LoggerFactory.getLogger(TemperatureQueryService.class);

    private final TemperatureRepository temperatureRepository;

    private final TemperatureMapper temperatureMapper;

    public TemperatureQueryService(TemperatureRepository temperatureRepository, TemperatureMapper temperatureMapper) {
        this.temperatureRepository = temperatureRepository;
        this.temperatureMapper = temperatureMapper;
    }

    /**
     * Return a {@link List} of {@link TemperatureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TemperatureDTO> findByCriteria(TemperatureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Temperature> specification = createSpecification(criteria);
        return temperatureMapper.toDto(temperatureRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TemperatureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TemperatureDTO> findByCriteria(TemperatureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Temperature> specification = createSpecification(criteria);
        return temperatureRepository.findAll(specification, page).map(temperatureMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemperatureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Temperature> specification = createSpecification(criteria);
        return temperatureRepository.count(specification);
    }

    /**
     * Function to convert {@link TemperatureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Temperature> createSpecification(TemperatureCriteria criteria) {
        Specification<Temperature> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Temperature_.id));
            }
            if (criteria.getMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMin(), Temperature_.min));
            }
            if (criteria.getMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMax(), Temperature_.max));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Temperature_.description));
            }
            if (criteria.getRusticite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRusticite(), Temperature_.rusticite));
            }
            if (criteria.getPlantesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPlantesId(), root -> root.join(Temperature_.plantes, JoinType.LEFT).get(Plante_.id))
                    );
            }
        }
        return specification;
    }
}
