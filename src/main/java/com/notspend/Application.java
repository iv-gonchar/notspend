package com.notspend;

import com.notspend.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
@EnableAutoConfiguration()
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}