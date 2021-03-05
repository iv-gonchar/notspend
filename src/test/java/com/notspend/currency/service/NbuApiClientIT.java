package com.notspend.currency.service;

import com.notspend.currency.entity.ExchangeRate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Depends on 3rd party server. Used only to debug response processing")
class NbuApiClientIT {

    private final NbuApiClient client = new NbuApiClient();

    @Test
    void getExchangeRate() {
        Double expectedRate = 27.7564;

        Optional<ExchangeRate> exchangeRate = client.getExchangeRate("USD", LocalDate.of(2021, 03,05));
        assertTrue(exchangeRate.isPresent());
        assertEquals(expectedRate, exchangeRate.get().getRate());
    }
}