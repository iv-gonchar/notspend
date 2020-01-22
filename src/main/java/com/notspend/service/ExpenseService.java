package com.notspend.service;

import com.notspend.entity.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {
    void addExpense(Expense expense);
    Optional<Expense> getExpenseById(int id);
    void updateExpense(Expense expense);
    List<Expense> getAllExpenses();
    List<Expense> getExpensesDuringCurrentMonth();
    List<Expense> getIncomesDuringCurrentMonth();
    void deleteExpenseById(int id);

    String getCurrentMonthName();
    Double getSumOfExpenseForCurrentMonth();
    Double getSumOfIncomeForCurrentMonth();
    List<Expense> getAllIncomeDuringYear();
}
