package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Ordre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ordre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdreRepository extends JpaRepository<Ordre, Long>, JpaSpecificationExecutor<Ordre> {}
