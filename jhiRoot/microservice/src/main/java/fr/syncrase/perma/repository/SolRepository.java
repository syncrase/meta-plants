package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Sol;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SolRepository extends JpaRepository<Sol, Long>, JpaSpecificationExecutor<Sol> {}
