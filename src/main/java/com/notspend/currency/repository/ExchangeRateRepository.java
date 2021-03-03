package com.notspend.currency.repository;

import com.notspend.currency.entity.ExchangeRate;
import com.notspend.currency.entity.ExchangeRateId;
import com.notspend.entity.Currency;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRateRepository extends Repository<ExchangeRate, ExchangeRateId> {

    ExchangeRate save(ExchangeRate rate);

    Optional<ExchangeRate> findByBaseAndTargetAndExchangeDate(Currency base, Currency target, LocalDate exchangeDate);
}
