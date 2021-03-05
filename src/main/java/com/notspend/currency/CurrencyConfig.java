package com.notspend.currency;

import com.notspend.currency.repository.ExchangeRateRepository;
import com.notspend.currency.service.CacheableExchangeRateService;
import com.notspend.currency.service.ExchangeRateJpaService;
import com.notspend.currency.service.api.client.ExchangeApiClientFactory;
import com.notspend.service.persistance.ExchangeRateService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:currency.properties")
public class CurrencyConfig {

    @Bean
    public ExchangeRateService exchangeRateService(ExchangeRateRepository repository, Environment env) {
        ExchangeApiClientFactory factory = new ExchangeApiClientFactory(env);
        ExchangeRateService jpaService = new ExchangeRateJpaService(repository, factory);
        return new CacheableExchangeRateService(jpaService);
    }

}
