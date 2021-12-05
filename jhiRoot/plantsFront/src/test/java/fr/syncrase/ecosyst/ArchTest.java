package fr.syncrase.ecosyst;

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
            .importPackages("fr.syncrase.ecosyst");

        noClasses()
            .that()
            .resideInAnyPackage("fr.syncrase.ecosyst.service..")
            .or()
            .resideInAnyPackage("fr.syncrase.ecosyst.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..fr.syncrase.ecosyst.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
