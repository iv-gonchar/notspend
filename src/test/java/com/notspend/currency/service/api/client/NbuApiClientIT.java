package com.notspend.currency.service.api.client;

import com.notspend.currency.entity.ExchangeRate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Depends on 3rd party server. Used only to debug response processing")
class NbuApiClientIT {

    private static final String CURRENT_API = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode={code}&date={date}&json";

    private final NbuApiClient client = new NbuApiClient(CURRENT_API);

    @Test
    void getExchangeRate() {
        Double expectedRate = 27.7564;

        Optional<ExchangeRate> exchangeRate = client.getExchangeRate("USD", LocalDate.of(2021, 3, 5));
        assertTrue(exchangeRate.isPresent());
        assertEquals(expectedRate, exchangeRate.get().getRate());
    }
}