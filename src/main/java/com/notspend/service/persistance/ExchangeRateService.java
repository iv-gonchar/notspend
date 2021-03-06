package com.notspend.service.persistance;

import com.notspend.currency.entity.Currency;

public interface ExchangeRateService {

    double getExchangeRateToUah(Currency target);
}
