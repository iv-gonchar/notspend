package com.notspend.service.view;

import com.notspend.entity.Account;
import com.notspend.currency.entity.Currency;
import com.notspend.entity.Expense;
import com.notspend.service.persistance.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final ExchangeRateService exchangeRateService;

    @Override
    public double expenseSum(List<Expense> expenses) {
        double sum = 0.0;
        for (Expense expense : expenses) {
            Currency currency = expense.getCurrency();
            if (!currency.getCode().equals("UAH")) {
                double currencyRate = exchangeRateService.getExchangeRateToUah(currency);
                sum = sum + expense.getSum() * currencyRate;
            } else {
                sum = sum + expense.getSum();
            }
        }
        return sum;
    }

    @Override
    public double accountSum(List<Account> accounts) {
        double sum = 0.0;
        for (Account account : accounts) {
            Currency currency = account.getCurrency();
            if (!currency.getCode().equals("UAH")) {
                Double currencyRate = exchangeRateService.getExchangeRateToUah(currency);
                sum = sum + account.getSummary() * currencyRate;
            } else {
                sum = sum + account.getSummary();
            }
        }
        return sum;
    }
}
