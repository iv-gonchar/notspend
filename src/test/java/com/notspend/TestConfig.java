package com.notspend;

import com.notspend.config.AppConfig;
import com.notspend.config.DispatcherServletInitializer;
import com.notspend.config.SecurityConfig;
import com.notspend.config.SecurityWebApplicationInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(value = "com.notspend", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AppConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = DispatcherServletInitializer.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SecurityWebApplicationInitializer.class)
})
public class TestConfig {
}
