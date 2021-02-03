package com.notspend.service.impl.sync;

import com.notspend.TestConfig;
import com.notspend.TestDatabaseInitializer;
import com.notspend.service.ExpenseSyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureDataJpa
@ContextConfiguration(classes = TestConfig.class, initializers = TestDatabaseInitializer.class)
@TestPropertySource(locations = "classpath:test.properties")
class MonobankSyncServiceImplIT {

    @Autowired
    private ExpenseSyncService syncService;

    @Test
    void syncDataWithBankServer() {
        assertNotNull(syncService);
    }
}