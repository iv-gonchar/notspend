package com.notspend.service.view;

import com.notspend.entity.Account;
import com.notspend.entity.Expense;

import java.util.List;

public interface CalculationService {

    double expenseSum(List<Expense> expenses);

    double accountSum (List<Account> accounts);
}
