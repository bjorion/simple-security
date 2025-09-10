package org.jorion.simplesecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class SimpleSecurityAppTest {

    @Test
    void contextLoads() {
    }

    /**
     * Verify the <a href="https://www.baeldung.com/java-archunit-intro">architectural rules</a> are followed
     */
    @Test
    void createApplicationModuleModel() {

        var applicationModules = ApplicationModules.of(SimpleSecurityApp.class);
        // applicationModules.forEach(System.out::println);
        applicationModules.verify();
    }

    /**
     * Generate PlantUml diagrams
     */
    @Test
    void createModuleDocumentation() {

        var applicationModules = ApplicationModules.of(SimpleSecurityApp.class);
        new Documenter(applicationModules)
                .writeDocumentation()
                .writeIndividualModulesAsPlantUml();
    }
}