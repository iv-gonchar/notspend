package com.notspend.service.impl;

import com.notspend.entity.Expense;
import com.notspend.entity.Profit;
import com.notspend.service.ProfitService;
import com.notspend.util.CurrencyProcessor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfitServiceImpl implements ProfitService {

    @Override
    public List<Profit> getProfitTableDuringLastYear(List<Expense> currentYearIncome, List<Expense> currentYearExpense) {
        List<Profit> yearProfit = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (int i = 11; i >= 0; i--){
            LocalDate monthToSync = today.minus(i, ChronoUnit.MONTHS);
            Profit monthProfit = new Profit();

            double monthIncomeSum = currentYearIncome.stream()
                    .filter(e -> e.getDate().getMonthValue() == monthToSync.getMonthValue())
                    .filter(e -> e.getDate().getYear() == monthToSync.getYear())
                    .map(e -> e.getSum() * CurrencyProcessor.getCurrencyRateToUah(e.getCurrency().getCode()))
                    .mapToDouble(Double::doubleValue)
                    .sum();

            double monthExpenseSum = currentYearExpense.stream()
                    .filter(e -> e.getDate().getMonthValue() == monthToSync.getMonthValue())
                    .filter(e -> e.getDate().getYear() == monthToSync.getYear())
                    .map(e -> e.getSum() * CurrencyProcessor.getCurrencyRateToUah(e.getCurrency().getCode()))
                    .mapToDouble(Double::doubleValue)
                    .sum();

            monthProfit.setMonthIncome(monthIncomeSum);
            monthProfit.setMonthExpense(monthExpenseSum);
            monthProfit.setMonth(monthToSync.getMonth().name());
            monthProfit.setYear(monthToSync.getYear());

            yearProfit.add(monthProfit);
        }
        return yearProfit;
    }
}
