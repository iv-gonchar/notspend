package com.notspend.currency.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.notspend.currency.entity.ExchangeRate;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * National bank of Ukraine API client
 */
@CommonsLog
class NbuApiClient {

    private static final String HOST = "https://bank.gov.ua/";

    private static final String API = "NBUStatService/v1/statdirectory/exchange?";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    private final UriComponents uriComponents = UriComponentsBuilder
            .fromUriString(HOST + API)
            .queryParam("valcode", "{code}")
            .queryParam("date", "{date}")
            .queryParam("json")
            .encode()
            .build();

    public Optional<ExchangeRate> getExchangeRate(String target, LocalDate date) {
        URI uri = uriComponents.expand(target, date.format(formatter)).toUri();
        try {
            NbuExchangeRate rate = template.getForObject(uri, NbuExchangeRate.class);
            return Optional.of(rate.toExchangeRate());
        } catch (RestClientException e) {
            log.error("Currency server is down", e);
        }
        return Optional.empty();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NbuExchangeRate {
//
//        /**
//         * Currency number
//         */
//        private int r030;
//
//        /**
//         * Currency name
//         */
//        private String txt;

        /**
         * Currency code
         */
        private String cc;

        private double rate;

        private LocalDate exchangedate;

        public ExchangeRate toExchangeRate() {
            return new ExchangeRate("UAH", cc.toUpperCase(), exchangedate, rate);
        }

    }
}
