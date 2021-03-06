package com.notspend.sync;

import com.notspend.entity.Account;
import com.notspend.exception.AccountSyncFailedException;
import com.notspend.service.TransactionSyncService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Stub implementation which does nothing
 */
class TransactionSyncServiceStub implements TransactionSyncService {

    @Override
    public void syncDataWithBankServer(List<Account> accounts) throws AccountSyncFailedException {
        // no-op
    }
}
