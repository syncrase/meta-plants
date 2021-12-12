package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Variete;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Variete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VarieteRepository extends JpaRepository<Variete, Long>, JpaSpecificationExecutor<Variete> {}
