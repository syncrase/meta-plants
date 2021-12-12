package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousEspece;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousEspece entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousEspeceRepository extends JpaRepository<SousEspece, Long>, JpaSpecificationExecutor<SousEspece> {}
