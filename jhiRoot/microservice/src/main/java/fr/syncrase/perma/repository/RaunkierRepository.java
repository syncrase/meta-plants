package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Raunkier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Raunkier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaunkierRepository extends JpaRepository<Raunkier, Long>, JpaSpecificationExecutor<Raunkier> {}
