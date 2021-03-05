package com.notspend.currency.service.exchange;

import com.notspend.currency.entity.ExchangeRate;
import com.notspend.currency.repository.ExchangeRateRepository;
import com.notspend.currency.service.exchange.client.ExchangeApiClient;
import com.notspend.currency.service.exchange.client.ExchangeApiClientFactory;
import com.notspend.entity.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateJpaServiceTest {

    private static final Currency UAH = new Currency("Hryvnia", "UAH", 980, "");

    private static final Currency EUR = new Currency("Euro", "EUR", 978, "");

    private static final LocalDate DATE = LocalDate.of(2021, 3, 5);

    @Mock
    private ExchangeRateRepository repository;

    @Mock
    private ExchangeApiClientFactory factory;

    @Mock
    private ExchangeApiClient client;

    private ExchangeRateJpaService exchangeRateJpaService;

    @BeforeEach
    void setUp() {
        exchangeRateJpaService = new ExchangeRateJpaService(repository, factory);
    }

    @Test
    void getExchangeRateFromRepository() {
        Double expectedRate = 33.4062;
        ExchangeRate persistedExchangeRate = new ExchangeRate("UAH", "EUR", DATE, expectedRate);
        when(repository.findByBaseAndTargetAndExchangeDate("UAH", "EUR", DATE)).thenReturn(Optional.of(persistedExchangeRate));
        Double actualRate = exchangeRateJpaService.getExchangeRate(UAH, EUR, DATE);

        assertEquals(expectedRate, actualRate);
        verifyZeroInteractions(factory);
    }

    @Test
    void getExchangeRateFromApi() {
        Double expectedRate = 33.4062;
        ExchangeRate apiExchangeRate = new ExchangeRate("UAH", "EUR", DATE, expectedRate);
        when(repository.findByBaseAndTargetAndExchangeDate("UAH", "EUR", DATE)).thenReturn(Optional.empty());
        when(factory.createClient("UAH")).thenReturn(client);
        when(client.getExchangeRate("EUR", DATE)).thenReturn(Optional.of(apiExchangeRate));

        Double actualRate = exchangeRateJpaService.getExchangeRate(UAH, EUR, DATE);

        assertEquals(expectedRate, actualRate);
        verify(repository, times(1)).findByBaseAndTargetAndExchangeDate(any(), any(), any());
        verify(repository, times(1)).save(apiExchangeRate);
    }

    @Test
    void getExchangeRateReturnsZero() {
        Double expectedRate = 0.0;
        when(factory.createClient("UAH")).thenReturn(client);

        Double actualRate = exchangeRateJpaService.getExchangeRate(UAH, EUR, DATE);
        assertEquals(expectedRate, actualRate);
        verify(repository, times(1)).findByBaseAndTargetAndExchangeDate(any(), any(), any());
    }
}
