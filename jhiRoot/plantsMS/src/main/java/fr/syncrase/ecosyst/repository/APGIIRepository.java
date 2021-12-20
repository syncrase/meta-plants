package fr.syncrase.ecosyst.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the APGII entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIIRepository extends JpaRepository<APGII, Long>, JpaSpecificationExecutor<APGII> {}
