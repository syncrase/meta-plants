package fr.syncrase.ecosyst;

import fr.syncrase.ecosyst.PlantsFrontApp;
import fr.syncrase.ecosyst.ReactiveSqlTestContainerExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = PlantsFrontApp.class)
@ExtendWith(ReactiveSqlTestContainerExtension.class)
public @interface IntegrationTest {
}
