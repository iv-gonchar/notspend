package com.notspend.currency.service;

import com.notspend.entity.Currency;
import com.notspend.service.persistance.ExchangeRateService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CacheableExchangeRateService implements ExchangeRateService {

    private final ExchangeRateService delegate;

    private final Map<Currency, CacheValue> cache = new HashMap<>();

    @Override
    public double getExchangeRateToUah(Currency target) {
        CacheValue value = cache.get(target);
        if (value != null && value.isFresh()) {
            return value.getRate();
        } else {
            double freshRate = delegate.getExchangeRateToUah(target);
            cache.put(target, new CacheValue(freshRate));
            return freshRate;
        }
    }

    @Data
    private static class CacheValue {

        private final double rate;

        private final LocalDate lastUpdated;

        public CacheValue(double rate) {
            this.rate = rate;
            this.lastUpdated = LocalDate.now();
        }

        public boolean isFresh() {
            return LocalDate.now().equals(lastUpdated);
        }
    }
}
