package com.notspend.repository;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainerProvider;

/**
 * Starts MySQL test container and set datasource properties in test ApplicationContext
 * during Spring context initialization
 */
public class RepositoryTestsInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        JdbcDatabaseContainer mySqlContainer = (JdbcDatabaseContainer) new MySQLContainerProvider()
                .newInstance("8.0.23")
                .withDatabaseName("notspenddb")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);
        mySqlContainer.start();

        TestPropertyValues.of(
                "spring.datasource.url=" + mySqlContainer.getJdbcUrl(),
                "spring.datasource.username=" + mySqlContainer.getUsername(),
                "spring.datasource.password=" + mySqlContainer.getPassword()
        ).applyTo(applicationContext);
    }

}
