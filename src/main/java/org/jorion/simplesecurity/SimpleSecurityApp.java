package org.jorion.simplesecurity;

import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
@PropertySource(value = "classpath:META-INF/build-info.properties", ignoreResourceNotFound = true)
public class SimpleSecurityApp {

    static void main(String[] args) {

        SpringApplication.run(SimpleSecurityApp.class, args);
    }

    @Bean
    @SuppressWarnings("unused")
    CommandLineRunner commandLineRunner(ObservationRegistry observationRegistry) {

        return args -> log.info("ARGS {}", Arrays.toString(args));
    }

}
