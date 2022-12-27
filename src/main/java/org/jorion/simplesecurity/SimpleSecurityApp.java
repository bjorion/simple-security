package org.jorion.simplesecurity;

import org.jorion.simplesecurity.config.RsaKeyProperties;
import org.jorion.simplesecurity.entity.INoOpEntity;
import org.jorion.simplesecurity.repository.INoOpRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = INoOpRepository.class)
@EntityScan(basePackageClasses = INoOpEntity.class)
@EnableConfigurationProperties(RsaKeyProperties.class)
@PropertySource("classpath:META-INF/build-info.properties")
public class SimpleSecurityApp
{
    public static void main(String[] args)
    {
        SpringApplication.run(SimpleSecurityApp.class, args);
    }
}
