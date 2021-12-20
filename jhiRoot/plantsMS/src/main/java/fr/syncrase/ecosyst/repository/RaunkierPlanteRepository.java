package fr.syncrase.ecosyst.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RaunkierPlante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaunkierPlanteRepository extends JpaRepository<RaunkierPlante, Long>, JpaSpecificationExecutor<RaunkierPlante> {}
