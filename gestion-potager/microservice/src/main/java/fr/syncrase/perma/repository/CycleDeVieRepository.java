package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.CycleDeVie;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CycleDeVie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CycleDeVieRepository extends JpaRepository<CycleDeVie, Long>, JpaSpecificationExecutor<CycleDeVie> {
}
