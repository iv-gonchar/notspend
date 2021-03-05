package com.notspend.currency.service.exchange.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates appropriate {@link ExchangeApiClient} for specified currency
 */
public class ExchangeApiClientFactory {

    private final Map<String, ExchangeApiClient> clients = new HashMap<>();

    public ExchangeApiClientFactory() {
        registerClients();
    }

    private void registerClients() {
        clients.put("UAH", new NbuApiClient());
    }

    public ExchangeApiClient createClient(String currencyCode) {
        return clients.get(currencyCode);
    }
}
