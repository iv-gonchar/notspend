package com.notspend.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.notspend.repository")
@EntityScan("com.notspend.entity")
public class RepositoryTestsConfig {
}
