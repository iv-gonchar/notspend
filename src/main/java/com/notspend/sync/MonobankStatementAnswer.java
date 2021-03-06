package com.notspend.sync;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(of = "id")
class MonobankStatementAnswer {

    private String id;

    private long time;

    private String description;

    private int mcc;

    private int amount;

    private int currencyCode;

    private int cashback;

    private String errorDescription;
}