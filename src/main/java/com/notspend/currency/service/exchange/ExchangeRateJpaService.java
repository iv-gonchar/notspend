package com.notspend.currency.service.exchange;

import com.notspend.currency.entity.ExchangeRate;
import com.notspend.currency.repository.ExchangeRateRepository;
import com.notspend.currency.service.exchange.client.ExchangeApiClient;
import com.notspend.currency.service.exchange.client.ExchangeApiClientFactory;
import com.notspend.entity.Currency;

import java.time.LocalDate;
import java.util.Optional;

public class ExchangeRateJpaService {

    private final ExchangeRateRepository repository;

    private final ExchangeApiClientFactory factory;

    public ExchangeRateJpaService(ExchangeRateRepository repository, ExchangeApiClientFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public double getExchangeRate(Currency base, Currency target) {
        return getExchangeRate(base, target, LocalDate.now());
    }

    double getExchangeRate(Currency base, Currency target, LocalDate date) {
        if (!base.getCode().equals("UAH")) {
            throw new IllegalArgumentException(String.format("Currency %s not supported by ExchangeRateService", base.getCode()));
        }
        Optional<ExchangeRate> rate = repository.findByBaseAndTargetAndExchangeDate(base.getCode(), target.getCode(), date);
        if (rate.isPresent()) {
            return rate.get().getRate();
        } else {
            return getFromApiAndSave(base, target, date);
        }
    }

    private double getFromApiAndSave(Currency base, Currency target, LocalDate date) {
        ExchangeApiClient client = factory.createClient(base.getCode());
        Optional<ExchangeRate> rate = client.getExchangeRate(target.getCode(), date);
        if (rate.isPresent()) {
            repository.save(rate.get());
            return rate.get().getRate();
        } else {
            return 0.0;
        }
    }

}
