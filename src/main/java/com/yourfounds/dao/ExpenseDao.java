package com.yourfounds.dao;

import com.yourfounds.entity.Expense;

import java.util.Date;
import java.util.List;

public interface ExpenseDao extends CrudDao<Expense,Integer> {
    Double getSumExpensesBetweenDates(Date dateFrom, Date dateTo);
    Double getSumIncomeBetweenDates(Date dateFrom, Date dateTo);
    List<Expense> getAllExpenseBetweenDates(Date dateFrom, Date dateTo);
}
