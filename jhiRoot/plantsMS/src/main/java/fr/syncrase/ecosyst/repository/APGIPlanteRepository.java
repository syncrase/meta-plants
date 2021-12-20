package fr.syncrase.ecosyst.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the APGIPlante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIPlanteRepository extends JpaRepository<APGIPlante, Long>, JpaSpecificationExecutor<APGIPlante> {}
