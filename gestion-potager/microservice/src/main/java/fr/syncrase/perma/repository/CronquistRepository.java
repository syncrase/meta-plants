package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Cronquist;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Cronquist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronquistRepository extends JpaRepository<Cronquist, Long>, JpaSpecificationExecutor<Cronquist> {
}
