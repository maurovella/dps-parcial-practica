package edu.itba.class10.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("nosql")
@EnableMongoRepositories(basePackages = "edu.itba.class10.infrastructure.persistence.data")
public class NoSqlConfig {
}
