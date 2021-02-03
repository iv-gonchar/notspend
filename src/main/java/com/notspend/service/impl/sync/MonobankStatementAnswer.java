package com.notspend.service.impl.sync;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@RequiredArgsConstructor
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