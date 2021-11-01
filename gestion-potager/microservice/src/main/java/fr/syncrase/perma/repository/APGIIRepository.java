package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.APGII;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the APGII entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIIRepository extends JpaRepository<APGII, Long>, JpaSpecificationExecutor<APGII> {
}
