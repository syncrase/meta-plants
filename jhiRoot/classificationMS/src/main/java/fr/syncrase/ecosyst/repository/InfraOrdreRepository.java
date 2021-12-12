package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.InfraOrdre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InfraOrdre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfraOrdreRepository extends JpaRepository<InfraOrdre, Long>, JpaSpecificationExecutor<InfraOrdre> {}
