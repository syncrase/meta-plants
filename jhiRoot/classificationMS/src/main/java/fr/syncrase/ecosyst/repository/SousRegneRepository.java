package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousRegne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousRegne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousRegneRepository extends JpaRepository<SousRegne, Long>, JpaSpecificationExecutor<SousRegne> {}
