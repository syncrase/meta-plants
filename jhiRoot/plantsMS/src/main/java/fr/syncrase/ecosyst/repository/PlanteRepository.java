package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Plante;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Plante entity.
 */
@Repository
public interface PlanteRepository extends JpaRepository<Plante, Long>, JpaSpecificationExecutor<Plante> {
    @Query(
        value = "select distinct plante from Plante plante left join fetch plante.nomsVernaculaires",
        countQuery = "select count(distinct plante) from Plante plante"
    )
    Page<Plante> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct plante from Plante plante left join fetch plante.nomsVernaculaires")
    List<Plante> findAllWithEagerRelationships();

    @Query("select plante from Plante plante left join fetch plante.nomsVernaculaires where plante.id =:id")
    Optional<Plante> findOneWithEagerRelationships(@Param("id") Long id);
}
