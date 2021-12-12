package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Espece;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Espece entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EspeceRepository extends JpaRepository<Espece, Long>, JpaSpecificationExecutor<Espece> {}
