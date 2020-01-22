package com.notspend.service;

import com.notspend.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    void addAccount(Account account);
    List<Account> getAccounts();
    Optional<Account> getAccount(int id);
    void deleteAccountById(int id);
    void updateAccount(Account account);

    boolean isAccountHaveRelations(int id);
    int replaceAccountInAllExpenses(int fromAccountId, int toAccountId);
    boolean transferMoneyBetweenAccounts(int fromAccountId, int toAccountId, Double sum);
    boolean transferMoneyBetweenAccountsWithDifferentCurrency(int fromAccountId, int toAccountId, Double from, Double to);
}
