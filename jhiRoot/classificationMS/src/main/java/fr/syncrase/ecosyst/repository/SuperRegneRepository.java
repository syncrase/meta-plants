package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SuperRegne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SuperRegne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuperRegneRepository extends JpaRepository<SuperRegne, Long>, JpaSpecificationExecutor<SuperRegne> {}
