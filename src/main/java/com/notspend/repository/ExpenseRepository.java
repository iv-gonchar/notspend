package com.notspend.repository;

import com.notspend.entity.Account;
import com.notspend.entity.Expense;
import com.notspend.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends CrudByUserRepository<Expense, Integer> {

    boolean existsByAccountAccountIdAndUser(int accountId, User user);

    List<Expense> findByCategoryIncomeAndDateBetweenAndUser(boolean income, LocalDate dateFrom, LocalDate dateTo, User user);

    @Modifying //(clearAutomatically = true) should be uncommented in case of errors
    // todo remove user from arguments by using SpEL instead
    @Query("UPDATE Expense SET account = ?2 WHERE account = ?1 AND user = ?3")
    long updateAccountInExpenses(Account oldAccount, Account newAccount, User user);

    // Double getSumExpensesBetweenDates(LocalDate dateFrom, LocalDate dateTo);
}
