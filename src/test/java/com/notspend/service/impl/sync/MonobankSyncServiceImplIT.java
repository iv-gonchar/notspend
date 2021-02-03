package com.notspend.service.impl.sync;

import com.notspend.TestConfig;
import com.notspend.TestDatabaseInitializer;
import com.notspend.dao.AccountDao;
import com.notspend.entity.Account;
import com.notspend.exception.AccountSyncFailedException;
import com.notspend.service.ExpenseSyncService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(SpringExtension.class)
@AutoConfigureDataJpa
@ContextConfiguration(classes = TestConfig.class, initializers = TestDatabaseInitializer.class)
@TestPropertySource(locations = "classpath:test.properties")
class MonobankSyncServiceImplIT {

    @Autowired
    private ExpenseSyncService syncService;

    @Autowired
    private AccountDao accountDao;

    private static ClientAndServer mockServer;

    @BeforeAll
    static void beforeAll() {
        mockServer = startClientAndServer(1080);
        new MockServerClient("localhost", 1080).when(
                request()
                        .withMethod("GET")
                        .withPath("/personal/statement/0/[0-9]{10}/[0-9]{10}"))
                .respond(
                        response()
                                .withDelay(TimeUnit.MILLISECONDS, 1500)
                                .withBody("[\n" +
                                        "  {\n" +
                                        "    \"id\": \"EuHWzqkKGVo=\",\n" +
                                        "    \"time\": 1612303623,\n" +
                                        "    \"description\": \"2 Cinema tickets\",\n" +
                                        "    \"mcc\": 7832,\n" +
                                        "    \"hold\": false,\n" +
                                        "    \"amount\": -24000,\n" +
                                        "    \"operationAmount\": -24000,\n" +
                                        "    \"currencyCode\": 980,\n" +
                                        "    \"commissionRate\": 0,\n" +
                                        "    \"cashbackAmount\": 0,\n" +
                                        "    \"balance\": 10050000,\n" +
                                        "    \"comment\": \"Avengers ticket\",\n" +
                                        "    \"receiptId\": \"XXXX-XXXX-XXXX-XXXX\",\n" +
                                        "    \"counterEdrpou\": \"1234567890\",\n" +
                                        "    \"counterIban\": \"UA898999980000123456789012345\"\n" +
                                        "  },\n" +
                                        "  {\n" +
                                        "    \"id\": \"DuHWzqkKGVo=\",\n" +
                                        "    \"time\": 1612256823,\n" +
                                        "    \"description\": \"T-short\",\n" +
                                        "    \"mcc\": 5611,\n" +
                                        "    \"hold\": false,\n" +
                                        "    \"amount\": -20000,\n" +
                                        "    \"operationAmount\": -20000,\n" +
                                        "    \"currencyCode\": 980,\n" +
                                        "    \"commissionRate\": 0,\n" +
                                        "    \"cashbackAmount\": 0,\n" +
                                        "    \"balance\": 10050000,\n" +
                                        "    \"comment\": \"my new t-short\",\n" +
                                        "    \"receiptId\": \"XXXX-XXXX-XXXX-XXXX\",\n" +
                                        "    \"counterEdrpou\": \"1234567890\",\n" +
                                        "    \"counterIban\": \"UA898999980000123456789012345\"\n" +
                                        "  },\n" +
                                        "  {\n" +
                                        "    \"id\": \"CuHWzqkKGVo=\",\n" +
                                        "    \"time\": 1612200396,\n" +
                                        "    \"description\": \"new PC\",\n" +
                                        "    \"mcc\": 5045,\n" +
                                        "    \"hold\": false,\n" +
                                        "    \"amount\": -4535000,\n" +
                                        "    \"operationAmount\": -4535000,\n" +
                                        "    \"currencyCode\": 980,\n" +
                                        "    \"commissionRate\": 0,\n" +
                                        "    \"cashbackAmount\": 0,\n" +
                                        "    \"balance\": 10050000,\n" +
                                        "    \"comment\": \"bought a new personal computer\",\n" +
                                        "    \"receiptId\": \"XXXX-XXXX-XXXX-XXXX\",\n" +
                                        "    \"counterEdrpou\": \"1234567890\",\n" +
                                        "    \"counterIban\": \"UA898999980000123456789012345\"\n" +
                                        "  },  \n" +
                                        "  {\n" +
                                        "    \"id\": \"BuHWzqkKGVo=\",\n" +
                                        "    \"time\": 1612178136,\n" +
                                        "    \"description\": \"Food in Silpo\",\n" +
                                        "    \"mcc\": 5411,\n" +
                                        "    \"hold\": false,\n" +
                                        "    \"amount\": -35445,\n" +
                                        "    \"operationAmount\": -35445,\n" +
                                        "    \"currencyCode\": 980,\n" +
                                        "    \"commissionRate\": 0,\n" +
                                        "    \"cashbackAmount\": 0,\n" +
                                        "    \"balance\": 10050000,\n" +
                                        "    \"comment\": \"Silpo\",\n" +
                                        "    \"receiptId\": \"XXXX-XXXX-XXXX-XXXX\",\n" +
                                        "    \"counterEdrpou\": \"1234567890\",\n" +
                                        "    \"counterIban\": \"UA898999980000123456789012345\"\n" +
                                        "  },\n" +
                                        "  {\n" +
                                        "    \"id\": \"AuHWzqkKGVo=\",\n" +
                                        "    \"time\": 1612173649,\n" +
                                        "    \"description\": \"coffee\",\n" +
                                        "    \"mcc\": 5812,\n" +
                                        "    \"hold\": false,\n" +
                                        "    \"amount\": -6500,\n" +
                                        "    \"operationAmount\": -6500,\n" +
                                        "    \"currencyCode\": 980,\n" +
                                        "    \"commissionRate\": 0,\n" +
                                        "    \"cashbackAmount\": 0,\n" +
                                        "    \"balance\": 10050000,\n" +
                                        "    \"comment\": \"bought some coffee\",\n" +
                                        "    \"receiptId\": \"XXXX-XXXX-XXXX-XXXX\",\n" +
                                        "    \"counterEdrpou\": \"1234567890\",\n" +
                                        "    \"counterIban\": \"UA898999980000123456789012345\"\n" +
                                        "  }\n" +
                                        "]"));
    }

    @Test
    @Transactional
    void syncDataWithBankServer() throws AccountSyncFailedException {
        Account accountToSync = accountDao.get(1);
        accountToSync.setToken("dummy_token");
        accountToSync.setSynchronizationTime(1612173649l);
        accountToSync.setSynchronizationId("AuHWzqkKGVo=");


        List<Account> accountsToSync = new ArrayList<>();
        accountsToSync.add(accountToSync);
        assertNotNull(syncService);
        syncService.syncDataWithBankServer(accountsToSync);
    }

    @AfterAll
    static void afterAll() {
        mockServer.stop();
    }
}