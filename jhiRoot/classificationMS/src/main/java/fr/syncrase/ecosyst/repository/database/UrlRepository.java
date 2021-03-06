package fr.syncrase.ecosyst.repository.database;

import fr.syncrase.ecosyst.domain.classification.entities.database.Url;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Url entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UrlRepository extends JpaRepository<Url, Long>, JpaSpecificationExecutor<Url> {}
