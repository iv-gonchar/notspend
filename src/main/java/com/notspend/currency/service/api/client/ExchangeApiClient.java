package com.notspend.currency.service.api.client;

import com.notspend.currency.entity.ExchangeRate;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Common interface for Exchange API of different banks
 */
public interface ExchangeApiClient {

    Optional<ExchangeRate> getExchangeRate(String target, LocalDate date);
}
