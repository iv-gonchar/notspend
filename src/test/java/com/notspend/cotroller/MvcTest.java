package com.notspend.cotroller;

import com.notspend.TestDatabaseInitializer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// makes Spring Boot to autoconfigure required beans for Hibernate, JPA repositories, Security context etc
// see @ImportAutoConfiguration. You may find all autoconfiguration classes applied in spring.factories file
@AutoConfigureMockMvc
@AutoConfigureDataJpa
// specifies configuration for our beans and initializer which starts MySql in container
@ContextConfiguration(classes = ControllerTestConfig.class, initializers = TestDatabaseInitializer.class)
// runs junits with Spring extension
@ExtendWith(SpringExtension.class)
// specify test properties file location
@TestPropertySource(locations = "classpath:application.properties")
// specifies that Web Application context should be created with default location for web files "src/main/webapp"
@WebAppConfiguration
public @interface MvcTest {
}
