package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousForme;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousForme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousFormeRepository extends JpaRepository<SousForme, Long>, JpaSpecificationExecutor<SousForme> {}
