package com.notspend.currency.entity;

import com.notspend.entity.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@IdClass(ExchangeRateId.class)
@Table(name = "exchange_rate")
@Data
@EqualsAndHashCode(of = {"base", "target", "exchangeDate"})
public class ExchangeRate {

    @Id
    @ManyToOne
    @JoinColumn(name = "base_currency")
    private Currency base;

    @Id
    @ManyToOne
    @JoinColumn(name = "target_currency")
    private Currency target;

    @Id
    @Column(name = "exchange_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate exchangeDate;

    @Column(name = "rate")
    private Double rate;

}
