package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Exposition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Exposition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpositionRepository extends JpaRepository<Exposition, Long>, JpaSpecificationExecutor<Exposition> {}
