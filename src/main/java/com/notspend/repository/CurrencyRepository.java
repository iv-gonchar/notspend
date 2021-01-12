package com.notspend.repository;

import com.notspend.entity.Currency;
import com.notspend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency, String> {

    List<Currency> findByUser(User user);

    Optional<Currency> findByNumber(Integer number);
}
