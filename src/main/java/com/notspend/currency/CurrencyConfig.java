package com.notspend.currency;

import com.notspend.currency.repository.ExchangeRateRepository;
import com.notspend.currency.service.ExchangeRateJpaService;
import com.notspend.currency.service.api.client.ExchangeApiClientFactory;
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
