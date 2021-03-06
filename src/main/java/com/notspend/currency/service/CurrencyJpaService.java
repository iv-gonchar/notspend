package com.notspend.currency.service;

import com.notspend.currency.entity.Currency;
import com.notspend.currency.repository.CurrencyRepository;
import com.notspend.service.persistance.CurrencyService;
import com.notspend.service.persistance.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Profile({"default", "jpa"})
public class CurrencyJpaService implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    private final UserService userService;

    @Override
    public Currency getCurrencyByCode(String code) {
        return currencyRepository.getByCode(code).orElseThrow(
                () -> new NoSuchElementException("There is no currency with code " + code + " in repository")
        );
    }

    @Override
    public Currency getCurrencyByNumber(Integer number) {
        return currencyRepository.getByNumber(number).orElseThrow(
                () -> new NoSuchElementException("There is no currency with number " + number + " in repository")
        );
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public List<Currency> getAllCurrenciesAssignedToUser() {
        return currencyRepository.getAssignedToUser(userService.currentUser());
    }
}
