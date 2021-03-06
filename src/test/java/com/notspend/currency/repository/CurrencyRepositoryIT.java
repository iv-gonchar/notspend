package com.notspend.currency.repository;

import com.notspend.currency.entity.Currency;
import com.notspend.repository.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO replace demo data from liquibase/changelogs/17_add_demo_data.sql with test data
@RepositoryTest
class CurrencyRepositoryIT {

    private static final String TEST_USERNAME = "demo";

    @Autowired
    private CurrencyRepository currencyRepository;

    /**
     * Check that all required currencies were added to database at start-up
     */
    @Test
    void findAll() {
        List<Currency> currencies = currencyRepository.findAll();
        assertThat(currencies.size()).isEqualTo(112);
    }

    @Test
    void getAssignedToUser() {
        List<Currency> assignedToUser = currencyRepository.getAssignedToUser(TEST_USERNAME);
        assertThat(assignedToUser.size()).isEqualTo(2);
        assertEquals("UAH", assignedToUser.get(0).getCode());
        assertEquals("USD", assignedToUser.get(1).getCode());
    }

    @Test
    void getByCode() {
        String expectedCode = "UAH";
        Currency uah = currencyRepository.getByCode(expectedCode).get();
        assertEquals(expectedCode, uah.getCode());
    }

    @Test
    void getByNonExistentCodeReturnsEmptyOptional() {
        assertTrue(currencyRepository.getByCode("nonExistentCode").isEmpty());
    }

    @Test
    void getByNumber() {
        Integer expectedNumber = 978;
        Currency eur = currencyRepository.getByNumber(expectedNumber).get();
        assertEquals(expectedNumber, eur.getNumber());
        assertEquals("EUR", eur.getCode());
    }

    @Test
    void getByNonExistentNumberReturnsEmptyOptional() {
        Integer nonExistentCurrencyNumber = 7;
        assertTrue(currencyRepository.getByNumber(nonExistentCurrencyNumber).isEmpty());
    }
}