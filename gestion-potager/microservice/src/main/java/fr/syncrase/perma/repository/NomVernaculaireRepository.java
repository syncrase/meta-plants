package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.NomVernaculaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NomVernaculaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NomVernaculaireRepository extends JpaRepository<NomVernaculaire, Long>, JpaSpecificationExecutor<NomVernaculaire> {}
