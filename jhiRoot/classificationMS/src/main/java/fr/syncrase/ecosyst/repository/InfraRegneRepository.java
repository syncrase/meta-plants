package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.InfraRegne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InfraRegne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfraRegneRepository extends JpaRepository<InfraRegne, Long>, JpaSpecificationExecutor<InfraRegne> {}
