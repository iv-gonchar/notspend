package com.notspend.service.view;

import com.notspend.entity.Expense;
import com.notspend.entity.Profit;
import com.notspend.service.persistance.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfitServiceImpl implements ProfitService {

    private final ExchangeRateService exchangeRateService;

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
                    .map(e -> e.getSum() * exchangeRateService.getExchangeRateToUah(e.getCurrency()))
                    .mapToDouble(Double::doubleValue)
                    .sum();

            double monthExpenseSum = currentYearExpense.stream()
                    .filter(e -> e.getDate().getMonthValue() == monthToSync.getMonthValue())
                    .filter(e -> e.getDate().getYear() == monthToSync.getYear())
                    .map(e -> e.getSum() * exchangeRateService.getExchangeRateToUah(e.getCurrency()))
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
