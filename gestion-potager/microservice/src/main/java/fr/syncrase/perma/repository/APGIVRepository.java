package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.APGIV;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the APGIV entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIVRepository extends JpaRepository<APGIV, Long>, JpaSpecificationExecutor<APGIV> {
}
