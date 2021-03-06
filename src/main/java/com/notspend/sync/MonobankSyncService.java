package com.notspend.sync;

import com.notspend.entity.Account;
import com.notspend.entity.Category;
import com.notspend.entity.Expense;
import com.notspend.service.TransactionSyncService;
import com.notspend.service.persistance.AccountService;
import com.notspend.service.persistance.CategoryService;
import com.notspend.service.persistance.ExpenseService;
import com.notspend.service.persistance.MccService;
import com.notspend.util.TimeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@CommonsLog
@RequiredArgsConstructor
class MonobankSyncService implements TransactionSyncService {

    /**
     * 10 minutes in secords
     */
    private static final int TEN_MINUTES = 10 * 60;

    /**
     * Synchronization period, which is equal 1 month in seconds
     */
    private static final int SYNC_PERIOD = 2682000;

    private final ExpenseService expenseService;

    private final CategoryService categoryService;

    private final AccountService accountService;

    private final MccService mccService;

    private final MonobankApiClient client;

    @Override
    public void syncDataWithBankServer(List<Account> accounts) {
        accounts.stream()
                .filter(a -> a.getToken() != null && !a.getToken().isEmpty())
                .forEach(this::syncAccount);
    }

    private void syncAccount(Account account) {
        long delayBetweenSync = TEN_MINUTES + 1;
        if (account.getSynchronizationTime() != null) {
            delayBetweenSync = TimeHelper.getCurrentEpochTime() - account.getSynchronizationTime();
        }

        if (delayBetweenSync < TEN_MINUTES) {
            account.setSynchronizationTime(TimeHelper.getCurrentEpochTime());
            accountService.updateAccount(account);
            return;
        }

        long instantTo = TimeHelper.getCurrentEpochTime();
        long instantFrom = instantTo - SYNC_PERIOD;

        if (account.getSynchronizationTime() == null) {
            syncFirstTime(account, instantFrom, instantTo);
            return;
        }

        String firstSuccessfulSyncId = null;
        String lastSuccessSyncId = account.getSynchronizationId();

        MonobankStatement[] statements = client.getStatements(account, instantFrom, instantTo);
        for (MonobankStatement statement : statements) {
            if (!statement.getId().equals(lastSuccessSyncId)) {
                String mccCategoryName = mccService.getCategoryByMccCode(statement.getMcc());
                if (mccCategoryName.isEmpty()) {
                    continue;
                }
                Expense expense = new Expense();
                expense.setUser(account.getUser());
                expense.setAccount(account);
                expense.setUser(account.getUser());
                expense.setCurrency(account.getCurrency());
                expense.setComment(statement.getDescription());
                expense.setSum(-(statement.getAmount() / 100d));

                List<Category> categories = categoryService.getAllExpenseCategories();
                Category category = categories.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(mccCategoryName))
                        .findFirst()
                        .orElse((new Category()));

                if (category.getId() == 0) {
                    category.setUser(account.getUser());
                    category.setIncome(false);
                    category.setName(mccCategoryName);
                    categoryService.addCategory(category);
                }

                expense.setCategory(categoryService.getCategory(category.getName()));

                long epochTime = statement.getTime();
                LocalDate date = LocalDate.ofInstant(Instant.ofEpochSecond(epochTime), ZoneId.systemDefault());
                expense.setDate(date);
                expense.setTime(LocalTime.ofSecondOfDay(epochTime % 3600));

                expenseService.addExpense(expense);
                if (firstSuccessfulSyncId == null) {
                    firstSuccessfulSyncId = statement.getId();
                }
            } else {
                //we find last sync id
                if (firstSuccessfulSyncId != null) {
                    account.setSynchronizationId(firstSuccessfulSyncId);
                }
                account.setSynchronizationTime(TimeHelper.getCurrentEpochTime());
                accountService.updateAccount(account);
                return;
            }
        }
    }

    private void syncFirstTime(Account account, long from, long to) {
        String lastTransactionId = null;
        try {
            MonobankStatement[] statements = client.getStatements(account, from, to);
            lastTransactionId = getLastTransactionId(statements);
        } catch (Exception e) {
            log.warn("Can't get last transaction id" + e);
        }
        account.setSynchronizationId(lastTransactionId);
        account.setSynchronizationTime(TimeHelper.getCurrentEpochTime());
        accountService.updateAccount(account);
    }

    private String getLastTransactionId(MonobankStatement[] statements) {
        if (statements.length == 0) {
            return null;
        } else {
            return statements[0].getId();
        }
    }
}
