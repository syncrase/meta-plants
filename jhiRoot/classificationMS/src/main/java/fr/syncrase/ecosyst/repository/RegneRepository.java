package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Regne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Regne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegneRepository extends JpaRepository<Regne, Long>, JpaSpecificationExecutor<Regne> {}
