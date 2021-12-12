package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousClasse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousClasse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousClasseRepository extends JpaRepository<SousClasse, Long>, JpaSpecificationExecutor<SousClasse> {}
