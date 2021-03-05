package com.notspend.currency.service.exchange.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates appropriate {@link ExchangeApiClient} for specified currency
 */
@Component
@PropertySource("classpath:currency.properties")
public class ExchangeApiClientFactory {

    private static final String PROPS_PREFIX = "notspend.currency.bank-api.";

    private final Environment env;

    private final Map<String, ExchangeApiClient> clients = new HashMap<>();

    public ExchangeApiClientFactory(Environment env) {
        this.env = env;
        registerClients();
    }

    private void registerClients() {
        clients.put("UAH", new NbuApiClient(getApi("NBU")));
    }

    public ExchangeApiClient createClient(String currencyCode) {
        return clients.get(currencyCode);
    }

    private String getApi(String bank) {
        return env.getProperty(PROPS_PREFIX + bank);
    }
}
