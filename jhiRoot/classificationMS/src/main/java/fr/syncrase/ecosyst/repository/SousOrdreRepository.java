package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousOrdre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousOrdre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousOrdreRepository extends JpaRepository<SousOrdre, Long>, JpaSpecificationExecutor<SousOrdre> {}
