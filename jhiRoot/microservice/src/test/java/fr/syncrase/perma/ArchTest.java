package fr.syncrase.perma;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("fr.syncrase.perma");

        noClasses()
            .that()
            .resideInAnyPackage("fr.syncrase.perma.service..")
            .or()
            .resideInAnyPackage("fr.syncrase.perma.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..fr.syncrase.perma.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
