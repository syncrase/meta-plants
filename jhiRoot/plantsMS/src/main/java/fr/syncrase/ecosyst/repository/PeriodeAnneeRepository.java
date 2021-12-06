package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PeriodeAnnee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodeAnneeRepository extends JpaRepository<PeriodeAnnee, Long>, JpaSpecificationExecutor<PeriodeAnnee> {}
