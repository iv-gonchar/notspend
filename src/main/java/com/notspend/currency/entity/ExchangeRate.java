package com.notspend.currency.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@IdClass(ExchangeRateId.class)
@Table(name = "exchange_rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"base", "target", "exchangeDate"})
public class ExchangeRate {

    @Id
    @Column(name = "base_currency")
    private String base;

    @Id
    @Column(name = "target_currency")
    private String target;

    @Id
    @Column(name = "exchange_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate exchangeDate;

    @Column(name = "rate")
    private Double rate;

}
