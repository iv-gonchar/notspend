package com.notspend.service.sync;

import com.notspend.entity.Account;
import com.notspend.exception.AccountSyncFailedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Stub implementation which does nothing
 */
@Service
@Profile("stub")
public class ExpenseSyncServiceStub implements ExpenseSyncService {

    @Override
    public void syncDataWithBankServer(List<Account> accounts) throws AccountSyncFailedException {
        // no-op
    }
}
