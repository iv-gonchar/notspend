package com.notspend.currency.repository;

import com.notspend.currency.entity.ExchangeRate;
import com.notspend.repository.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RepositoryTest
class ExchangeRateRepositoryIT {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LocalDate date = LocalDate.of(2021, 3, 3);

    @Test
    void save() {
        Double expectedRate = 2.6003;

        ExchangeRate sekToUah = new ExchangeRate("UAH", "SEK", date, expectedRate);
        exchangeRateRepository.save(sekToUah);
        entityManager.flush();

        Double savedRate = jdbcTemplate.queryForObject(
                "SELECT rate FROM exchange_rate WHERE base_currency = ? AND target_currency = ?",
                new Object[]{"UAH", "SEK"}, Double.class);
        assertEquals(expectedRate, savedRate);
    }

    @Test
    void findByBaseAndTargetAndExchangeDate() {
        Double expectedRate = 33.6006;

        ExchangeRate eurToUah = exchangeRateRepository.findByBaseAndTargetAndExchangeDate("UAH", "EUR", date).get();
        assertEquals(expectedRate, eurToUah.getRate());
    }
}