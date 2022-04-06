package fr.syncrase.ecosyst.service.database;

import fr.syncrase.ecosyst.domain.classification.entities.database.CronquistRank;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CronquistRank}.
 */
public interface CronquistRankService {
    /**
     * Save a cronquistRank.
     *
     * @param cronquistRank the entity to save.
     * @return the persisted entity.
     */
    CronquistRank save(CronquistRank cronquistRank);

    /**
     * Partially updates a cronquistRank.
     *
     * @param cronquistRank the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CronquistRank> partialUpdate(CronquistRank cronquistRank);

    /**
     * Get all the cronquistRanks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CronquistRank> findAll(Pageable pageable);

    /**
     * Get the "id" cronquistRank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CronquistRank> findOne(Long id);

    /**
     * Delete the "id" cronquistRank.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
