package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SuperDivision;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SuperDivision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuperDivisionRepository extends JpaRepository<SuperDivision, Long>, JpaSpecificationExecutor<SuperDivision> {}
