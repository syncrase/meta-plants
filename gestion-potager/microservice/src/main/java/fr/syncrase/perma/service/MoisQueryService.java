package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Mois;
import fr.syncrase.perma.repository.MoisRepository;
import fr.syncrase.perma.service.criteria.MoisCriteria;
import fr.syncrase.perma.service.dto.MoisDTO;
import fr.syncrase.perma.service.mapper.MoisMapper;
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
 * Service for executing complex queries for {@link Mois} entities in the database.
 * The main input is a {@link MoisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MoisDTO} or a {@link Page} of {@link MoisDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MoisQueryService extends QueryService<Mois> {

    private final Logger log = LoggerFactory.getLogger(MoisQueryService.class);

    private final MoisRepository moisRepository;

    private final MoisMapper moisMapper;

    public MoisQueryService(MoisRepository moisRepository, MoisMapper moisMapper) {
        this.moisRepository = moisRepository;
        this.moisMapper = moisMapper;
    }

    /**
     * Return a {@link List} of {@link MoisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MoisDTO> findByCriteria(MoisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mois> specification = createSpecification(criteria);
        return moisMapper.toDto(moisRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MoisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MoisDTO> findByCriteria(MoisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mois> specification = createSpecification(criteria);
        return moisRepository.findAll(specification, page).map(moisMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MoisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mois> specification = createSpecification(criteria);
        return moisRepository.count(specification);
    }

    /**
     * Function to convert {@link MoisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mois> createSpecification(MoisCriteria criteria) {
        Specification<Mois> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Mois_.id));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumero(), Mois_.numero));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Mois_.nom));
            }
        }
        return specification;
    }
}
