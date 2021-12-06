package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.APGI;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the APGI entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIRepository extends JpaRepository<APGI, Long>, JpaSpecificationExecutor<APGI> {}
