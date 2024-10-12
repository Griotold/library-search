package com.library.service

import com.library.repository.BookRepository
import spock.lang.Specification

class BookQueryServiceTest extends Specification {
    BookRepository naverBookRepository = Mock(BookRepository)
    BookRepository kakaoBookRepository = Mock(BookRepository)

    BookQueryService bookQueryService

    void setup() {
        bookQueryService = new BookQueryService(naverBookRepository, kakaoBookRepository)
    }

    // 파라미터가 그대로 넘어가는지에 대한 검증은 꼭 필요하다. 안 그런 경우도 있다고 함.
    def "search시 인자가 그대로 넘어가고 naver쪽으로 호출한다."() {
        given:
        def givenQuery = "HTTP완벽가이드"
        def givenPage = 1
        def givenSize = 10

        when:
        bookQueryService.search(givenQuery, givenPage, givenSize)

        then:
        1 * naverBookRepository.search(*_) >> {
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }

        and: "정상 동작시 카카오쪽으로 호출하지 않는다."
        0 * kakaoBookRepository.search(*_)
    }
}
