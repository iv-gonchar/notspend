package com.notspend.currency.entity;

import com.notspend.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents composite primary key of ExchangeRate entity
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExchangeRateId implements Serializable {

    private Currency base;

    private Currency target;

    private LocalDate exchangeDate;
}
