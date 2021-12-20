package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.ClassificationCronquist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassificationCronquist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassificationCronquistRepository
    extends JpaRepository<ClassificationCronquist, Long>, JpaSpecificationExecutor<ClassificationCronquist> {}
