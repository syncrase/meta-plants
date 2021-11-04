package fr.syncrase.perma.service;

import fr.syncrase.perma.domain.*; // for static metamodels
import fr.syncrase.perma.domain.Feuillage;
import fr.syncrase.perma.repository.FeuillageRepository;
import fr.syncrase.perma.service.criteria.FeuillageCriteria;
import fr.syncrase.perma.service.dto.FeuillageDTO;
import fr.syncrase.perma.service.mapper.FeuillageMapper;
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
 * Service for executing complex queries for {@link Feuillage} entities in the database.
 * The main input is a {@link FeuillageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FeuillageDTO} or a {@link Page} of {@link FeuillageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FeuillageQueryService extends QueryService<Feuillage> {

    private final Logger log = LoggerFactory.getLogger(FeuillageQueryService.class);

    private final FeuillageRepository feuillageRepository;

    private final FeuillageMapper feuillageMapper;

    public FeuillageQueryService(FeuillageRepository feuillageRepository, FeuillageMapper feuillageMapper) {
        this.feuillageRepository = feuillageRepository;
        this.feuillageMapper = feuillageMapper;
    }

    /**
     * Return a {@link List} of {@link FeuillageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FeuillageDTO> findByCriteria(FeuillageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Feuillage> specification = createSpecification(criteria);
        return feuillageMapper.toDto(feuillageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FeuillageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FeuillageDTO> findByCriteria(FeuillageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Feuillage> specification = createSpecification(criteria);
        return feuillageRepository.findAll(specification, page).map(feuillageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FeuillageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Feuillage> specification = createSpecification(criteria);
        return feuillageRepository.count(specification);
    }

    /**
     * Function to convert {@link FeuillageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Feuillage> createSpecification(FeuillageCriteria criteria) {
        Specification<Feuillage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Feuillage_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Feuillage_.type));
            }
            if (criteria.getPlanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPlanteId(), root -> root.join(Feuillage_.plantes, JoinType.LEFT).get(Plante_.id))
                    );
            }
        }
        return specification;
    }
}
