package com.notspend.repository;

import com.notspend.entity.Account;
import com.notspend.entity.Expense;
import com.notspend.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends CrudByUserRepository<Expense, Integer> {

    Long countByAccountAndUser(Account account, User user);

    List<Expense> findByCategoryIncomeAndDateBetweenAndUser(boolean income, LocalDate dateFrom, LocalDate dateTo, User user);

    // Double getSumExpensesBetweenDates(LocalDate dateFrom, LocalDate dateTo);
}
