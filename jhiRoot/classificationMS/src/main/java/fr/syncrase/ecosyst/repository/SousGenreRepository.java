package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.SousGenre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SousGenre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousGenreRepository extends JpaRepository<SousGenre, Long>, JpaSpecificationExecutor<SousGenre> {}
