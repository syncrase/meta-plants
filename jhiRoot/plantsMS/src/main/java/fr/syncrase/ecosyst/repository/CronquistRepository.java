package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Cronquist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cronquist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronquistRepository extends JpaRepository<Cronquist, Long>, JpaSpecificationExecutor<Cronquist> {}
