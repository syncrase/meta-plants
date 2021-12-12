package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.InfraClasse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InfraClasse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfraClasseRepository extends JpaRepository<InfraClasse, Long>, JpaSpecificationExecutor<InfraClasse> {}
