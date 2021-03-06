package com.notspend.sync;

import com.notspend.service.TransactionSyncService;
import com.notspend.service.persistance.AccountService;
import com.notspend.service.persistance.CategoryService;
import com.notspend.service.persistance.ExpenseService;
import com.notspend.service.persistance.MccService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:sync.properties")
public class SyncConfig {

    @Bean
    public TransactionSyncService transactionSyncService(
            AccountService accountService,
            CategoryService categoryService,
            ExpenseService expenseService,
            MccService mccService,
            Environment env) {

        String syncEnabled = env.getProperty("notspend.sync.enabled");
        if ("true".equalsIgnoreCase(syncEnabled)) {
            String api = env.getProperty("notspend.sync.bank-api.monobank");
            MonobankApiClient client = new MonobankApiClient(api);
            return new MonobankSyncService(expenseService, categoryService, accountService, mccService, client);
        } else {
            return new TransactionSyncServiceStub();
        }
    }

}
