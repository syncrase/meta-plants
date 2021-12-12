package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.MicroOrdre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MicroOrdre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MicroOrdreRepository extends JpaRepository<MicroOrdre, Long>, JpaSpecificationExecutor<MicroOrdre> {}
