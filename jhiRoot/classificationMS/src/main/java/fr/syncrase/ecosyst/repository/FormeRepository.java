package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Forme;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Forme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormeRepository extends JpaRepository<Forme, Long>, JpaSpecificationExecutor<Forme> {}
