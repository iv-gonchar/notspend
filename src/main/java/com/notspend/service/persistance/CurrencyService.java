package com.notspend.service.persistance;

import com.notspend.currency.entity.Currency;

import java.util.List;

public interface CurrencyService {

    Currency getCurrencyByCode(String code);
    Currency getCurrencyByNumber(Integer number);
    List<Currency> getAllCurrencies();
    List<Currency> getAllCurrenciesAssignedToUser();
}
