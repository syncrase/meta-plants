package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Reproduction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Reproduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReproductionRepository extends JpaRepository<Reproduction, Long>, JpaSpecificationExecutor<Reproduction> {}
