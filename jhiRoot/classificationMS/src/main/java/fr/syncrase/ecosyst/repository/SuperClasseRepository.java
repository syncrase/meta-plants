package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SuperClasse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SuperClasse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuperClasseRepository extends JpaRepository<SuperClasse, Long>, JpaSpecificationExecutor<SuperClasse> {}
