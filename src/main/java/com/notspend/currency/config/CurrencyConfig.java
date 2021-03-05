package com.notspend.currency.config;

import com.notspend.currency.repository.ExchangeRateRepository;
import com.notspend.currency.service.exchange.ExchangeRateJpaService;
import com.notspend.currency.service.exchange.client.ExchangeApiClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:currency.properties")
public class CurrencyConfig {

    @Bean
    public ExchangeRateJpaService exchangeRateService(ExchangeRateRepository repository, Environment env) {
        ExchangeApiClientFactory factory = new ExchangeApiClientFactory(env);
        return new ExchangeRateJpaService(repository, factory);
    }

}
