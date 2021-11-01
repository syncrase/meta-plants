package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.Ressemblance;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Ressemblance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RessemblanceRepository extends JpaRepository<Ressemblance, Long>, JpaSpecificationExecutor<Ressemblance> {
}
