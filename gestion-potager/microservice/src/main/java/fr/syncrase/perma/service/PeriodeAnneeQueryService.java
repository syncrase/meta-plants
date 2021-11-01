package fr.syncrase.perma.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fr.syncrase.perma.domain.PeriodeAnnee;
import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.repository.PeriodeAnneeRepository;
import fr.syncrase.perma.service.dto.PeriodeAnneeCriteria;
import fr.syncrase.perma.service.dto.PeriodeAnneeDTO;
import fr.syncrase.perma.service.mapper.PeriodeAnneeMapper;

/**
 * Service for executing complex queries for {@link PeriodeAnnee} entities in the database.
 * The main input is a {@link PeriodeAnneeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PeriodeAnneeDTO} or a {@link Page} of {@link PeriodeAnneeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeriodeAnneeQueryService extends QueryService<PeriodeAnnee> {

    private final Logger log = LoggerFactory.getLogger(PeriodeAnneeQueryService.class);

    private final PeriodeAnneeRepository periodeAnneeRepository;

    private final PeriodeAnneeMapper periodeAnneeMapper;

    public PeriodeAnneeQueryService(PeriodeAnneeRepository periodeAnneeRepository, PeriodeAnneeMapper periodeAnneeMapper) {
        this.periodeAnneeRepository = periodeAnneeRepository;
        this.periodeAnneeMapper = periodeAnneeMapper;
    }

    /**
     * Return a {@link List} of {@link PeriodeAnneeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PeriodeAnneeDTO> findByCriteria(PeriodeAnneeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PeriodeAnnee> specification = createSpecification(criteria);
        return periodeAnneeMapper.toDto(periodeAnneeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PeriodeAnneeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PeriodeAnneeDTO> findByCriteria(PeriodeAnneeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PeriodeAnnee> specification = createSpecification(criteria);
        return periodeAnneeRepository.findAll(specification, page)
            .map(periodeAnneeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeriodeAnneeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PeriodeAnnee> specification = createSpecification(criteria);
        return periodeAnneeRepository.count(specification);
    }

    /**
     * Function to convert {@link PeriodeAnneeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PeriodeAnnee> createSpecification(PeriodeAnneeCriteria criteria) {
        Specification<PeriodeAnnee> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PeriodeAnnee_.id));
            }
            if (criteria.getDebutId() != null) {
                specification = specification.and(buildSpecification(criteria.getDebutId(),
                    root -> root.join(PeriodeAnnee_.debut, JoinType.LEFT).get(Mois_.id)));
            }
            if (criteria.getFinId() != null) {
                specification = specification.and(buildSpecification(criteria.getFinId(),
                    root -> root.join(PeriodeAnnee_.fin, JoinType.LEFT).get(Mois_.id)));
            }
        }
        return specification;
    }
}
