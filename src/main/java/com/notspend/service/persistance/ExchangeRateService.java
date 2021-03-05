package com.notspend.service.persistance;

import com.notspend.entity.Currency;

public interface ExchangeRateService {

    double getExchangeRate(Currency base, Currency target);
}
