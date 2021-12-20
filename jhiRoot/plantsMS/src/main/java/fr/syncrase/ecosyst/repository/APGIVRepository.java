package fr.syncrase.ecosyst.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the APGIV entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIVRepository extends JpaRepository<APGIV, Long>, JpaSpecificationExecutor<APGIV> {}
