package com.notspend.currency.entity;

import com.notspend.entity.Currency;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents composite primary key of ExchangeRate entity
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExchangeRateId implements Serializable {

    private String base;

    private String target;

    private LocalDate exchangeDate;
}
