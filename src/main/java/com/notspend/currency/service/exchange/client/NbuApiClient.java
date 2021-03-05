package com.notspend.currency.service.exchange.client;

import com.fasterxml.jackson.annotation.JsonFormat;
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
class NbuApiClient implements ExchangeApiClient {

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
            NbuExchangeRate[] rates = template.getForObject(uri, NbuExchangeRate[].class);
            NbuExchangeRate rate = NbuExchangeRate.getFirst(rates);
            return Optional.of(rate.toExchangeRate());
        } catch (RestClientException e) {
            log.error("Exception happened during handling response from NBU API server", e);
        } catch (IllegalStateException e) {
            log.error("Wrong response was received", e);
        }
        return Optional.empty();
    }

    @Data
    private static class NbuExchangeRate {

        /**
         * Currency code
         */
        private String cc;

        private double rate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        private LocalDate exchangeDate;

        public ExchangeRate toExchangeRate() {
            return new ExchangeRate("UAH", cc.toUpperCase(), exchangeDate, rate);
        }

        public static NbuExchangeRate getFirst(NbuExchangeRate[] rates) {
            if (rates.length == 0) {
                throw new IllegalArgumentException("Response is empty. Check request parameters sent");
            }
            if (rates.length != 1) {
                throw new IllegalArgumentException("Response array size doesn't equal 1. Unexpected response");
            }
            return rates[0];
        }

    }

}
