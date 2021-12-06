package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Semis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Semis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemisRepository extends JpaRepository<Semis, Long>, JpaSpecificationExecutor<Semis> {}
