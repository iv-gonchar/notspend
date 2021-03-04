package com.notspend.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
@EnableJpaRepositories({"com.notspend.repository", "com.notspend.currency.repository"})
@EntityScan({"com.notspend.entity", "com.notspend.currency.entity"})
public class RepositoryTestsConfig {

}
