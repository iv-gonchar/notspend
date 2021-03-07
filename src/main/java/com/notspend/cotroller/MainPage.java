package com.notspend.cotroller;

import com.notspend.currency.entity.Currency;
import com.notspend.entity.Account;
import com.notspend.entity.Category;
import com.notspend.entity.Expense;
import com.notspend.entity.User;
import com.notspend.exception.AccountSyncFailedException;
import com.notspend.service.TransactionSyncService;
import com.notspend.service.persistance.CategoryService;
import com.notspend.service.persistance.ExchangeRateService;
import com.notspend.service.persistance.ExpenseService;
import com.notspend.service.persistance.UserService;
import com.notspend.service.view.CalculationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@CommonsLog
public class MainPage {

    @Autowired
    private CalculationService calculationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TransactionSyncService transactionSyncService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping(value = "/")
    public String getMainPage(HttpServletRequest request, User user) {
        List<Account> accountList = user.getAccounts();
        syncTransactions(accountList);

        request.getSession().setAttribute("username", user.getUsername());
        request.getSession().setAttribute("totalSum", String.format("%.2f", calculationService.accountSum(accountList)));

        List<Expense> expenseDuringCurrentMonth = expenseService.getExpensesDuringCurrentMonth();
        List<Expense> incomeDuringCurrentMonth = expenseService.getIncomesDuringCurrentMonth();

        request.getSession().setAttribute("spendCurrentMonth", String.format("%.2f", calculationService.expenseSum(expenseDuringCurrentMonth)));
        request.getSession().setAttribute("earnCurrentMonth", String.format("%.2f", calculationService.expenseSum(incomeDuringCurrentMonth)));

        computeDataForYearIncomeChart(request);
        computeDataForMonthExpenseChart(request);
        computeMonthExpensesByCategory(request);
        computeExchangeRates(request, accountList);
        return "index";
    }

    private void syncTransactions(List<Account> accounts) {
        List<Account> accountsToSync = accounts.stream().filter(a -> a.getToken() != null && !a.getToken().isEmpty()).collect(Collectors.toList());
        if (!accountsToSync.isEmpty()) {
            try {
                transactionSyncService.syncDataWithBankServer(accountsToSync);
            } catch (AccountSyncFailedException e) {
                log.error("Can't synchronize accounts." + e);
            }
        }
    }

    private void computeDataForYearIncomeChart(HttpServletRequest request) {
        List<Expense> incomeDuringLastYear = expenseService.getAllIncomeDuringYear();
        List<Double> incomeSumForEachMonth = new ArrayList<>();
        List<String> monthNames = new ArrayList<>();
        int monthFrom = LocalDate.now().getMonthValue() + 1;
        for (int i = monthFrom; i < monthFrom + 12; i++) {
            Double monthSum = 0d;
            int currentMonthNumber = getRightMonthOrder(i);
            for (Expense expense : incomeDuringLastYear) {
                if ((expense.getDate().getMonthValue()) == currentMonthNumber) {
                    monthSum += expense.getCurrency().getCode().equals("UAH") ? expense.getSum() : expense.getSum() * exchangeRateService.getExchangeRateToUah(expense.getCurrency());
                }
            }
            incomeSumForEachMonth.add(monthSum);
            String monthName = Month.of(currentMonthNumber).name();
            monthNames.add(monthName.substring(0, 3));
        }
        String income = incomeSumForEachMonth.toString().replaceFirst("\\[", "").replaceFirst("\\]", "");
        String months = monthNames.toString().replaceFirst("\\[", "").replaceFirst("\\]", "");

        request.getSession().setAttribute("income", income);
        request.getSession().setAttribute("months", months);
    }

    private void computeDataForMonthExpenseChart(HttpServletRequest request) {
        List<Expense> expensesThisMonth = expenseService.getExpensesDuringCurrentMonth();
        List<Double> expenseSumForEachDay = new ArrayList<>();
        List<String> dayNames = new ArrayList<>();
        for (int i = 1; i <= LocalDate.now().lengthOfMonth(); i++) {
            Double daySum = 0d;
            for (Expense expense : expensesThisMonth) {
                if ((expense.getDate().getDayOfMonth()) == i) {
                    daySum += expense.getCurrency().getCode().equals("UAH") ? expense.getSum() : expense.getSum() * exchangeRateService.getExchangeRateToUah(expense.getCurrency());
                }
            }
            expenseSumForEachDay.add(daySum);
            dayNames.add(String.valueOf(i));
        }
        String expensePerDay = expenseSumForEachDay.toString().replaceFirst("\\[", "").replaceFirst("\\]", "");
        String days = dayNames.toString().replaceFirst("\\[", "").replaceFirst("\\]", "");

        request.getSession().setAttribute("expensePerDay", expensePerDay);
        request.getSession().setAttribute("days", days);
    }

    private void computeMonthExpensesByCategory(HttpServletRequest request) {
        List<Expense> expensesThisMonth = expenseService.getExpensesDuringCurrentMonth();
        List<Category> categories = categoryService.getAllExpenseCategories();
        List<Double> expenseSumForEachCategory = new ArrayList<>();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            Double sumByCategory = 0d;
            for (Expense expense : expensesThisMonth) {
                if (expense.getCategory().getName().equals(category.getName())) {
                    sumByCategory += expense.getCurrency().getCode().equals("UAH") ? expense.getSum() : expense.getSum() * exchangeRateService.getExchangeRateToUah(expense.getCurrency());
                }
            }
            expenseSumForEachCategory.add(sumByCategory);
            categoryNames.add(category.getName());
        }

        String expenseSumForEachCategoryStr = expenseSumForEachCategory.toString().replaceFirst("\\[", "").replaceFirst("\\]", "");
        String categoryNamesStr = categoryNames.toString().replaceFirst("\\[", "").replaceFirst("\\]", "");

        request.getSession().setAttribute("expenseSumForEachCategory", expenseSumForEachCategoryStr);
        request.getSession().setAttribute("categoryNames", categoryNamesStr);
    }

    private void computeExchangeRates(HttpServletRequest request, List<Account> accountList) {
        Map<String, Double> currencyValues = accountList.stream()
                .map(Account::getCurrency)
                .distinct()
                .collect(Collectors.toMap(Currency::getCode, b -> exchangeRateService.getExchangeRateToUah(b)));

        request.getSession().setAttribute("currencyValues", currencyValues);
    }

    private int getRightMonthOrder(int monthNumber) {
        int month = monthNumber % 12;
        return month == 0 ? 12 : month;
    }
}
