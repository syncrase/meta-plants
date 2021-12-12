package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousVariete;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousVariete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousVarieteRepository extends JpaRepository<SousVariete, Long>, JpaSpecificationExecutor<SousVariete> {}
