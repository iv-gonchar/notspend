package com.notspend.service.jpa;

import com.notspend.entity.Account;
import com.notspend.repository.AccountRepository;
import com.notspend.repository.ExpenseRepository;
import com.notspend.service.AccountService;
import com.notspend.service.UserService;
import com.notspend.util.SecurityUserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Profile({"default", "jpa"})
public class AccountJpaService implements AccountService {

    private final AccountRepository accountRepository;

    private final ExpenseRepository expenseRepository;

    private final UserService userService;

    @Override
    public void addAccount(Account account) {
        validateAccountOwner(account);
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.getAllByUser(userService.currentUser());
    }

    @Override
    public Account getAccount(int id) {
        return accountRepository.getByIdAndUser(id, userService.currentUser()).orElseThrow(
                () -> new NoSuchElementException("There is no account with id " + id + " in repository " +
                        "for current user " + SecurityUserHandler.getCurrentUser())
        );
    }

    @Override
    public void deleteAccountById(int id) {
        accountRepository.deleteByIdAndUser(id, userService.currentUser());
    }

    public void deleteAccount(Account account) {
        validateAccountOwner(account);
        accountRepository.delete(account);
    }

    @Override
    public void updateAccount(Account account) {
        validateAccountOwner(account);
        accountRepository.save(account);
    }

    @Override
    public boolean isAccountHaveRelations(int accountId) {
        return expenseRepository.existsByAccountAccountIdAndUser(accountId, userService.currentUser());
    }

    @Override
    // todo maybe move to ExpenseJpaService?
    public int replaceAccountInAllExpenses(int fromAccountId, int toAccountId) {
        Account oldAccount = getAccount(fromAccountId);
        Account newAccount = getAccount(toAccountId);
        long updated = expenseRepository.updateAccountInExpenses(oldAccount, newAccount, userService.currentUser());
        double balance = oldAccount.getSummary();
        transferMoney(oldAccount, newAccount, balance);
        // todo maybe remove this line, caller should call both methods?
        deleteAccount(oldAccount);
        // todo change interface to long
        return (int) updated;
    }

    @Override
    // todo change method name to shorter
    @Deprecated
    public boolean transferMoneyBetweenAccounts(int fromAccountId, int toAccountId, Double amount) {
        Account fromAccount = getAccount(fromAccountId);
        Account toAccount = getAccount(toAccountId);
        return transferMoney(fromAccount, toAccount, amount);
    }

    private boolean transferMoney(Account fromAccount, Account toAccount, Double amount) {
        validateAmount(amount);
        fromAccount.substract(amount);
        toAccount.plus(amount);
        updateAccount(fromAccount);
        updateAccount(toAccount);
        return true;
    }

    private void validateAmount(Double amount) {
        if (0.0 > amount) {
            throw new IllegalArgumentException("Sum should not be negative");
        }
    }

    @Override
    // todo change API to method with shorter name
    @Deprecated
    public boolean transferMoneyBetweenAccountsWithDifferentCurrency(int fromAccountId, int toAccountId, Double from, Double to) {
        return transferMoney(fromAccountId, toAccountId, from, to);
    }

    public boolean transferMoney(int fromAccountId, int toAccountId, double fromAmount, double toAmount) {
        Account fromAccount = getAccount(fromAccountId);
        Account toAccount = getAccount(toAccountId);
        fromAccount.substract(fromAmount);
        toAccount.plus(toAmount);
        updateAccount(fromAccount);
        updateAccount(toAccount);
        return true;
    }

    private void validateAccountOwner(Account account) {
        if (!userService.currentUser().equals(account.getUser())) {
            throw new IllegalArgumentException("Operation with another user's account is prohibited");
        }
    }

}
