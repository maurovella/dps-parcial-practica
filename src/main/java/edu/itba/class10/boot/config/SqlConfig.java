package edu.itba.class10.boot.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile("sql")
@Configuration
@EntityScan(basePackages = "edu.itba.class10.infrastructure.persistence.relational")
@EnableJpaRepositories(basePackages = "edu.itba.class10.infrastructure.persistence.relational")
public class SqlConfig {
}
