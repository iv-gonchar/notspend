package com.notspend.repository;

import com.notspend.entity.Currency;
import com.notspend.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends Repository<Currency, String> {

    List<Currency> getAll();

    @Query("SELECT DISTINCT cur FROM Currency AS cur WHERE EXISTS " +
            "(SELECT acc FROM Account AS acc WHERE acc.currency = cur AND acc.user = ?1)")
    List<Currency> getAssignedToUser(User user);

    Optional<Currency> getByCode(String code);

    Optional<Currency> getByNumber(Integer number);
}
