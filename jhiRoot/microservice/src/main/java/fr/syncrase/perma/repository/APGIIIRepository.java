package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.APGIII;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the APGIII entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIIIRepository extends JpaRepository<APGIII, Long>, JpaSpecificationExecutor<APGIII> {}
