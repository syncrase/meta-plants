package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.APGIIPlante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the APGIIPlante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIIPlanteRepository extends JpaRepository<APGIIPlante, Long>, JpaSpecificationExecutor<APGIIPlante> {}
