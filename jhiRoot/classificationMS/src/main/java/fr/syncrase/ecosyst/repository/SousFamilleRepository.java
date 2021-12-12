package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousFamille;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousFamille entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousFamilleRepository extends JpaRepository<SousFamille, Long>, JpaSpecificationExecutor<SousFamille> {}
