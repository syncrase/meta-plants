package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassificationNom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassificationNomRepository extends JpaRepository<ClassificationNom, Long>, JpaSpecificationExecutor<ClassificationNom> {}
