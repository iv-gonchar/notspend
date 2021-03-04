package com.notspend.currency.service;

import com.notspend.currency.entity.ExchangeRate;
import com.notspend.currency.repository.ExchangeRateRepository;
import com.notspend.entity.Currency;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExchangeRateJpaService {

    private final ExchangeRateRepository repository;

    private final NbuApiClient client = new NbuApiClient();

    public ExchangeRateJpaService(ExchangeRateRepository repository) {
        this.repository = repository;
    }

    public double getExchangeRate(Currency base, Currency target) {
        return getExchangeRate(base, target, LocalDate.now());
    }

    private double getExchangeRate(Currency base, Currency target, LocalDate date) {
        if (!base.getCode().equals("UAH")) {
            throw new IllegalArgumentException(String.format("Currency %s not supported by ExchangeRateService", base.getCode()));
        }
        Optional<ExchangeRate> rate = repository.findByBaseAndTargetAndExchangeDate(base.getCode(), target.getCode(), date);
        if (rate.isPresent()) {
            return rate.get().getRate();
        } else {
            rate = client.getExchangeRate(target.getCode(), date);
            if (rate.isPresent()) {
                repository.save(rate.get());
                return rate.get().getRate();
            } else {
                return 0.0;
            }
        }
    }

}
