package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Germination;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Germination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GerminationRepository extends JpaRepository<Germination, Long>, JpaSpecificationExecutor<Germination> {
}
