package com.notspend.currency.service.exchange.client;

import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates appropriate {@link ExchangeApiClient} for specified currency
 */
public class ExchangeApiClientFactory {

    private static final String PROPS_PREFIX = "notspend.currency.bank-api.";

    private final Environment env;

    private final Map<String, ExchangeApiClient> clients = new HashMap<>();

    public ExchangeApiClientFactory(Environment env) {
        this.env = env;
        registerClients();
    }

    private void registerClients() {
        clients.put("UAH", new NbuApiClient(getApiUrl("NBU")));
    }

    private String getApiUrl(String bank) {
        return env.getProperty(PROPS_PREFIX + bank);
    }

    public ExchangeApiClient createClient(String currencyCode) {
        return clients.get(currencyCode);
    }

}
