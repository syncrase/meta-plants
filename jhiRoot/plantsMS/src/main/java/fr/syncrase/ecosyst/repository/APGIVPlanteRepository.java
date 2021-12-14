package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.APGIVPlante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the APGIVPlante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIVPlanteRepository extends JpaRepository<APGIVPlante, Long>, JpaSpecificationExecutor<APGIVPlante> {}
