package com.notspend.util;

import com.notspend.entity.Account;
import com.notspend.entity.Currency;
import com.notspend.entity.Expense;
import com.notspend.service.persistance.ExchangeRateService;

import java.util.List;

public class CalculationHelper {

    private CalculationHelper() {
    }

    public static Double expenseSum(List<Expense> expenses, ExchangeRateService exchangeRateService){
        Double sum = 0D;
        for (Expense expense : expenses){
            Currency currency = expense.getCurrency();
            //TODO: replace hardcoded currency
            if (!currency.getCode().equals("UAH")){
                Double currencyRate = exchangeRateService.getExchangeRateToUah(currency);
                sum = sum + expense.getSum() * currencyRate;
            } else {
                sum += expense.getSum();
            }
        }
        return sum;
    }

    public static Double accountSum (List<Account> accounts, ExchangeRateService exchangeRateService){
        Double sum = 0D;
        for (Account account : accounts){
            Currency currency = account.getCurrency();
            //TODO: replace hardcoded currency
            if (!currency.getCode().equals("UAH")){
                Double currencyRate = exchangeRateService.getExchangeRateToUah(currency);
                sum = sum + account.getSummary() * currencyRate;
            } else {
                sum += account.getSummary();
            }
        }
        return sum;
    }
}
