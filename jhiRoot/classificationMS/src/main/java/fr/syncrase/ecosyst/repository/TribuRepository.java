package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Tribu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tribu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TribuRepository extends JpaRepository<Tribu, Long>, JpaSpecificationExecutor<Tribu> {}
