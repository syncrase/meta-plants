package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SuperOrdre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SuperOrdre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuperOrdreRepository extends JpaRepository<SuperOrdre, Long>, JpaSpecificationExecutor<SuperOrdre> {}
