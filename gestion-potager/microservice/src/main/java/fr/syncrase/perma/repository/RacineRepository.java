package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Racine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Racine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RacineRepository extends JpaRepository<Racine, Long>, JpaSpecificationExecutor<Racine> {}
