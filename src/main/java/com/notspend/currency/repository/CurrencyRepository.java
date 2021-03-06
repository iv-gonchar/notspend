package com.notspend.currency.repository;

import com.notspend.currency.entity.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends Repository<Currency, String> {

    List<Currency> findAll();

    @Query("SELECT DISTINCT cur FROM Currency AS cur WHERE EXISTS " +
            "(SELECT acc FROM Account AS acc WHERE acc.currency = cur AND acc.user.username = ?1)")
    List<Currency> getAssignedToUser(String username);

    Optional<Currency> getByCode(String code);

    Optional<Currency> getByNumber(Integer number);
}
