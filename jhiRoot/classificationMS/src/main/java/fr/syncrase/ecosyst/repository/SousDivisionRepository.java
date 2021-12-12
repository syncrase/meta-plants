package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousDivision;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousDivision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousDivisionRepository extends JpaRepository<SousDivision, Long>, JpaSpecificationExecutor<SousDivision> {}
