package com.notspend.service.jpa;

import com.notspend.entity.Account;
import com.notspend.entity.Expense;
import com.notspend.repository.ExpenseRepository;
import com.notspend.service.AccountService;
import com.notspend.service.ExpenseService;
import com.notspend.service.UserService;
import com.notspend.util.DateHelper;
import com.notspend.util.SecurityUserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Profile({"default", "jpa"})
public class ExpenseJpaService implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final AccountService accountService;

    private final UserService userService;

    @Override
    public void addExpense(Expense expense) {
        validateExpense(expense);
        validateExpenseOwner(expense);
        Account account = expense.getAccount();
        if (expense.getCategory().isIncome()) {
            account.plus(expense.getSum());
        } else {
            account.substract(expense.getSum());
        }
        accountService.updateAccount(account);
        expenseRepository.save(expense);
    }

    @Override
    public Expense getExpenseById(int id) {
        return expenseRepository.getByIdAndUser(id, userService.currentUser()).orElseThrow(
                () -> new NoSuchElementException("There is no expense with id " + id + " in repository " +
                        "for current user " + SecurityUserHandler.getCurrentUser())
        );
    }

    @Override
    public void updateExpense(Expense expense) {
        validateExpense(expense);
        validateExpenseOwner(expense);
        expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = expenseRepository.getAllByUser(userService.currentUser());
        // todo use Spring Data JPA sort instead
        Collections.sort(expenses);
        return expenses;
    }

    @Override
    public List<Expense> getExpensesDuringCurrentMonth() {
        List<Expense> expenses = expenseRepository.getByCategoryIncomeAndDateBetweenAndUser(false,
                DateHelper.getFirstDayOfCurrentMonth(), DateHelper.getLastDayOfCurrentMonth(), userService.currentUser());
        // todo use Spring Data JPA sort instead
        Collections.sort(expenses);
        return expenses;
    }

    @Override
    public List<Expense> getIncomesDuringCurrentMonth() {
        List<Expense> expenses = expenseRepository.getByCategoryIncomeAndDateBetweenAndUser(true,
                DateHelper.getFirstDayOfCurrentMonth(), DateHelper.getLastDayOfCurrentMonth(), userService.currentUser());
        // todo use Spring Data JPA sort instead
        Collections.sort(expenses);
        return expenses;
    }

    @Override
    public void deleteExpenseById(int id) {
        Expense expense = getExpenseById(id);
        Account account = expense.getAccount();
        if (expense.getCategory().isIncome()) {
            account.minus(Math.abs(expense.getSum()));
        } else {
            account.plus(expense.getSum());
        }
        accountService.updateAccount(account);
        expenseRepository.deleteByIdAndUser(id, userService.currentUser());
    }

    @Override
    // todo move to DateHelper
    public String getCurrentMonthName() {
        return Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    @Override
    public Double getSumOfExpenseForCurrentMonth() {
        return Optional.of(expenseRepository.getExpenseSumBetweenDates(DateHelper.getFirstDayOfCurrentMonth(),
                DateHelper.getLastDayOfCurrentMonth(), userService.currentUser())).orElse(0.0);
    }

    @Override
    public Double getSumOfIncomeForCurrentMonth() {
        return Optional.of(expenseRepository.getIncomeSumBetweenDates(DateHelper.getFirstDayOfCurrentMonth(),
                DateHelper.getLastDayOfCurrentMonth(), userService.currentUser())).orElse(0.0);
    }

    @Override
    public List<Expense> getAllIncomeDuringYear() {
        LocalDate today = DateHelper.today();
        LocalDate yearAgo = DateHelper.yearAgo();
        return expenseRepository.getByCategoryIncomeAndDateBetweenAndUser(true, yearAgo, today,
                userService.currentUser());
    }

    @Override
    public List<Expense> getAllExpenseDuringYear() {
        LocalDate today = DateHelper.today();
        LocalDate yearAgo = DateHelper.yearAgo();
        return expenseRepository.getByCategoryIncomeAndDateBetweenAndUser(false, yearAgo, today,
                userService.currentUser());
    }

    // todo maybe move it to Expense class
    // todo use bean validation instead
    private void validateExpense(Expense expense) {
        if (expense.getSum() == null) {
            throw new IllegalArgumentException("Expense sum is required");
        }
        if (expense.getCategory() == null ) {
            throw new IllegalArgumentException("Expense category is required");
        }
        if (expense.getAccount() == null ) {
            throw new IllegalArgumentException("Expense account is required");
        }
        if (expense.getUser() == null ) {
            throw new IllegalArgumentException("Expense user is required");
        }
        if (expense.getCurrency() == null ) {
            throw new IllegalArgumentException("Expense currency is required");
        }
    }

    private void validateExpenseOwner(Expense expense) {
        if (!userService.currentUser().equals(expense.getUser())) {
            throw new IllegalArgumentException("Operation with another user's expense is prohibited");
        }
        if (!userService.currentUser().equals(expense.getAccount().getUser())) {
            throw new IllegalArgumentException("Operation with another user's account is prohibited");
        }
    }

}
