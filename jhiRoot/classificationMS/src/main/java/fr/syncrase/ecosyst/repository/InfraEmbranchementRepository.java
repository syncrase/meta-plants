package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.InfraEmbranchement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InfraEmbranchement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfraEmbranchementRepository
    extends JpaRepository<InfraEmbranchement, Long>, JpaSpecificationExecutor<InfraEmbranchement> {}
