package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SuperFamille;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SuperFamille entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuperFamilleRepository extends JpaRepository<SuperFamille, Long>, JpaSpecificationExecutor<SuperFamille> {}
