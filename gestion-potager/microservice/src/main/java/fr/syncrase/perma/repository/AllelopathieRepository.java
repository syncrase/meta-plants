package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Allelopathie;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Allelopathie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllelopathieRepository extends JpaRepository<Allelopathie, Long>, JpaSpecificationExecutor<Allelopathie> {
}
