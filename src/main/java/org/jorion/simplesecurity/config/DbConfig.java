package org.jorion.simplesecurity.config;

import org.jorion.simplesecurity.entity.INoOpEntity;
import org.jorion.simplesecurity.repository.INoOpRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = INoOpRepository.class)
@EntityScan(basePackageClasses = INoOpEntity.class)
public class DbConfig {
}
