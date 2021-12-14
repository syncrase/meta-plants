package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Clade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Clade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CladeRepository extends JpaRepository<Clade, Long>, JpaSpecificationExecutor<Clade> {}
