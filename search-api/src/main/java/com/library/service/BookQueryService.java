package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.repository.BookRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookQueryService {
    @Qualifier("naverBookRepository")
    private final BookRepository naverBookRepository;

    @Qualifier("kakaoBookRepository")
    private final BookRepository kakaoBookRepository;

    @CircuitBreaker(name = "naverSearch", fallbackMethod = "searchFallBack")
    public PageResult<SearchResponse> search(String query, int page, int size) {
        return naverBookRepository.search(query, page, size);
    }

    // 위의 메서드가 에러 발생시 동작
    public PageResult<SearchResponse> searchFallBack(String query, int page, int size, Throwable throwable) {
        if (throwable instanceof CallNotPermittedException) {
            return handleOpenCircuit(query, page, size);
        }

        return handleException(query, page, size, throwable);
    }

    // 써킷이 열렷다는 소리는 네이버 api가 문제가 있다는 소리니 카카오 api에 요청
    private PageResult<SearchResponse> handleOpenCircuit(String query, int page, int size) {
        log.warn("[BookQueryService] Circuit Breaker is open! Fallbakc to kakao search.");
        return kakaoBookRepository.search(query, page, size);
    }

    // 써킷이 안 열렸다는 거는 네이버 api 문제는 아님
    // 그래도 카카오로 호출하는 걸로 함
    private PageResult<SearchResponse> handleException(String query, int page, int size, Throwable throwable) {
        log.error("[BookQueryService] An error occurred! Fallback to kakao search. errorMessage={}", throwable.getMessage());
        return kakaoBookRepository.search(query, page, size);
    }
}
