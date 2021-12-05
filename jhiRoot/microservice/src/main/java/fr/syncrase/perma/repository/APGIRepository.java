package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.APGI;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the APGI entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIRepository extends JpaRepository<APGI, Long>, JpaSpecificationExecutor<APGI> {}
