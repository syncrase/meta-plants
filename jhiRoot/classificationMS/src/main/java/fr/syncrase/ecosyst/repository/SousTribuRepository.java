package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousTribu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousTribu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousTribuRepository extends JpaRepository<SousTribu, Long>, JpaSpecificationExecutor<SousTribu> {}
