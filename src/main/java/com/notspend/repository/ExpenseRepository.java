package com.notspend.repository;

import com.notspend.entity.Account;
import com.notspend.entity.Category;
import com.notspend.entity.Expense;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends CrudByUserRepository<Expense, Integer> {

    boolean existsByAccountIdAndUserUsername(int accountId, String username);

    boolean existsByCategoryIdAndUserUsername(int categoryId, String username);

    List<Expense> getByCategoryIncomeAndDateBetweenAndUserUsername(boolean income, LocalDate dateFrom, LocalDate dateTo, String username);

    @Modifying //(clearAutomatically = true) should be uncommented in case of errors
    // todo remove user from arguments by using SpEL instead
    @Query("UPDATE Expense SET account = ?2 WHERE account = ?1 AND user.username = ?3")
    int updateAccountInExpenses(Account oldAccount, Account newAccount, String username);

    @Modifying //(clearAutomatically = true) should be uncommented in case of errors
    @Query("UPDATE Expense SET category = ?2 WHERE category = ?1 AND user.username = ?3")
    int updateCategoryInExpenses(Category oldCategory, Category newCategory, String username);

    @Query("SELECT SUM(e.sum) FROM Expense e LEFT JOIN e.category " +
            "WHERE e.category.income = FALSE AND e.date BETWEEN ?1 AND ?2 AND e.user.username = ?3")
    Double getExpenseSumBetweenDates(LocalDate dateFrom, LocalDate dateTo, String username);

    @Query("SELECT SUM(e.sum) FROM Expense e LEFT JOIN e.category " +
            "WHERE e.category.income = TRUE AND e.date BETWEEN ?1 AND ?2 AND e.user.username = ?3")
    Double getIncomeSumBetweenDates(LocalDate dateFrom, LocalDate dateTo, String username);
}
