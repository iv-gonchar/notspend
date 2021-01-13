package com.notspend.repository;

import com.notspend.entity.Currency;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CurrencyRepository extends Repository<Currency, String> {

    Optional<Currency> findByNumber(Integer number);
}
