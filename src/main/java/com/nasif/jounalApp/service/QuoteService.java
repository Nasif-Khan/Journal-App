package com.nasif.jounalApp.service;

import com.nasif.jounalApp.api.response.QuoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.List;

@Component
public class QuoteService {
    private static final String apiKey =  "s+3PEXbCjWGyWWK2NkyA4g==ttGMKNnyj9Cpfvwo";
    private static final String api = "https://api.api-ninjas.com/v1/quotes";

    @Autowired
    private RestTemplate restTemplate;

    public List<QuoteResponse> getQuote(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<QuoteResponse>> response = restTemplate.exchange(
                api,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<QuoteResponse>>() {}
        );

        List<QuoteResponse> body = response.getBody();
        return body;

    }

}
