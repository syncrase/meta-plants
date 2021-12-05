package fr.syncrase.perma.repository;

import fr.syncrase.perma.domain.TypeSemis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeSemis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeSemisRepository extends JpaRepository<TypeSemis, Long>, JpaSpecificationExecutor<TypeSemis> {}
