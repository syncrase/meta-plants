package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.MicroEmbranchement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MicroEmbranchement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MicroEmbranchementRepository
    extends JpaRepository<MicroEmbranchement, Long>, JpaSpecificationExecutor<MicroEmbranchement> {}
