package fr.syncrase.ecosyst.repository.database;

import fr.syncrase.ecosyst.domain.classification.entities.database.CronquistRank;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data SQL repository for the CronquistRank entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronquistRankRepository extends JpaRepository<CronquistRank, Long>, JpaSpecificationExecutor<CronquistRank> {

}
