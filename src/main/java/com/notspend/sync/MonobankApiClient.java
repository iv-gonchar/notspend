package com.notspend.sync;

import com.notspend.entity.Account;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@CommonsLog
class MonobankApiClient {

    private static final int DEFAULT_CARD_NUMBER = 0;

    private final RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    private final UriComponents uriComponents;

    public MonobankApiClient(String api) {
        uriComponents = UriComponentsBuilder
                .fromUriString(api)
                .encode()
                .build();
    }

    public MonobankStatement[] getStatements(Account account, long from, long to) {
        URI uri = uriComponents.expand(DEFAULT_CARD_NUMBER, from, to).toUri();
        RequestEntity<Void> request = RequestEntity.get(uri)
                .header("X-Token", account.getToken())
                .build();
        try {
            ResponseEntity<MonobankStatement[]> response = template.exchange(request, MonobankStatement[].class);
            MonobankStatement[] statements = response.getBody();
            return statements;
        } catch (RestClientException e) {
            log.error("Exception occured during communication with Monobank API", e);
        } catch (Exception e) {
            log.error("Can't retrieve statements.", e);
        }
        return new MonobankStatement[0];
    }
}
