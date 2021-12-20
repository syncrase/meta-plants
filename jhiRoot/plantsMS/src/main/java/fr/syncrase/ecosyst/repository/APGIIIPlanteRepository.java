package fr.syncrase.ecosyst.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the APGIIIPlante entity.
 */
@Repository
public interface APGIIIPlanteRepository extends JpaRepository<APGIIIPlante, Long>, JpaSpecificationExecutor<APGIIIPlante> {
    @Query(
        value = "select distinct aPGIIIPlante from APGIIIPlante aPGIIIPlante left join fetch aPGIIIPlante.clades",
        countQuery = "select count(distinct aPGIIIPlante) from APGIIIPlante aPGIIIPlante"
    )
    Page<APGIIIPlante> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct aPGIIIPlante from APGIIIPlante aPGIIIPlante left join fetch aPGIIIPlante.clades")
    List<APGIIIPlante> findAllWithEagerRelationships();

    @Query("select aPGIIIPlante from APGIIIPlante aPGIIIPlante left join fetch aPGIIIPlante.clades where aPGIIIPlante.id =:id")
    Optional<APGIIIPlante> findOneWithEagerRelationships(@Param("id") Long id);
}
