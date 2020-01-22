package com.notspend.service;

import com.notspend.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {

    Optional<Currency> getCurrencyByCode(String code);
    List<Currency> getAllCurrencies();
    List<Currency> getAllCurrenciesAssignedToUser();
}
