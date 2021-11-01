package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Classification;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Classification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Long>, JpaSpecificationExecutor<Classification> {
}
