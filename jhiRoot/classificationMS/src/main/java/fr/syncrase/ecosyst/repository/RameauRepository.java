package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Rameau;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rameau entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RameauRepository extends JpaRepository<Rameau, Long>, JpaSpecificationExecutor<Rameau> {}
