package com.library;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverClient {
    private final RestClient restClient;
    private final String naverUrl;
    private final String naverClientId;
    private final String naverClientSecret;

    // public Service(@Value("${value.test}") String test) {

    public NaverClient(@Value("${external.naver.url}") String naverUrl,
                       @Value("${external.naver.headers.client-id}") String naverClientId,
                       @Value("${external.naver.headers.client-secret}") String naverClientSecret) {

        this.restClient = RestClient.create();
        this.naverUrl = naverUrl;
        this.naverClientId = naverClientId;
        this.naverClientSecret = naverClientSecret;
    }

    public String search(String query) {
        String url = UriComponentsBuilder.fromHttpUrl(naverUrl + "/v1/search/book.json")
                .queryParam("query", query)
                .queryParam("display", 1)
                .queryParam("start", 1)
                .toUriString();

        return restClient.get()
                .uri(url)
                .header("X-Naver-Client-Id", naverClientId)
                .header("X-Naver-Client-Secret", naverClientSecret)
                .retrieve()
                .body(String.class);
    }
}
