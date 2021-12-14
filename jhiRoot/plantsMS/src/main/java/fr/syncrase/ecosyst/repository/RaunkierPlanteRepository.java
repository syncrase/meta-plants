package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.RaunkierPlante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RaunkierPlante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaunkierPlanteRepository extends JpaRepository<RaunkierPlante, Long>, JpaSpecificationExecutor<RaunkierPlante> {}
