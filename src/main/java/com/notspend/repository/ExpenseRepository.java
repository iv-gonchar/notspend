package com.notspend.repository;

import com.notspend.entity.Expense;
import com.notspend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends CrudRepository<Expense, Integer> {

    List<Expense> findByUserAndCategoryIncomeAndDateBetween(User user, boolean income, LocalDate dateFrom, LocalDate dateTo);

    // Double getSumExpensesBetweenDates(LocalDate dateFrom, LocalDate dateTo);
}
