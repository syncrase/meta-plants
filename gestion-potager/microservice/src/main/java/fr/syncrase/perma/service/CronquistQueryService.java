package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Cronquist;
import fr.syncrase.perma.repository.CronquistRepository;
import fr.syncrase.perma.service.criteria.CronquistCriteria;
import fr.syncrase.perma.service.dto.CronquistDTO;
import fr.syncrase.perma.service.mapper.CronquistMapper;
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
 * Service for executing complex queries for {@link Cronquist} entities in the database.
 * The main input is a {@link CronquistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CronquistDTO} or a {@link Page} of {@link CronquistDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CronquistQueryService extends QueryService<Cronquist> {

    private final Logger log = LoggerFactory.getLogger(CronquistQueryService.class);

    private final CronquistRepository cronquistRepository;

    private final CronquistMapper cronquistMapper;

    public CronquistQueryService(CronquistRepository cronquistRepository, CronquistMapper cronquistMapper) {
        this.cronquistRepository = cronquistRepository;
        this.cronquistMapper = cronquistMapper;
    }

    /**
     * Return a {@link List} of {@link CronquistDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CronquistDTO> findByCriteria(CronquistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cronquist> specification = createSpecification(criteria);
        return cronquistMapper.toDto(cronquistRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CronquistDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CronquistDTO> findByCriteria(CronquistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cronquist> specification = createSpecification(criteria);
        return cronquistRepository.findAll(specification, page).map(cronquistMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CronquistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cronquist> specification = createSpecification(criteria);
        return cronquistRepository.count(specification);
    }

    /**
     * Function to convert {@link CronquistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cronquist> createSpecification(CronquistCriteria criteria) {
        Specification<Cronquist> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cronquist_.id));
            }
            if (criteria.getRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegne(), Cronquist_.regne));
            }
            if (criteria.getSousRegne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousRegne(), Cronquist_.sousRegne));
            }
            if (criteria.getDivision() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDivision(), Cronquist_.division));
            }
            if (criteria.getClasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClasse(), Cronquist_.classe));
            }
            if (criteria.getSousClasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSousClasse(), Cronquist_.sousClasse));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdre(), Cronquist_.ordre));
            }
            if (criteria.getFamille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamille(), Cronquist_.famille));
            }
            if (criteria.getGenre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenre(), Cronquist_.genre));
            }
            if (criteria.getEspece() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEspece(), Cronquist_.espece));
            }
        }
        return specification;
    }
}
