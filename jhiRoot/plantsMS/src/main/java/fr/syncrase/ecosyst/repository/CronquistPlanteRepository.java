package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.CronquistPlante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CronquistPlante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronquistPlanteRepository extends JpaRepository<CronquistPlante, Long>, JpaSpecificationExecutor<CronquistPlante> {}
