package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousSection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousSectionRepository extends JpaRepository<SousSection, Long>, JpaSpecificationExecutor<SousSection> {}
