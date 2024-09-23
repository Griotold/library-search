package com.library.service

import com.library.entity.DailyStat
import spock.lang.Specification

class BookApplicationServiceTest extends Specification {
    BookApplicationService bookApplicationService

    BookQueryService bookQueryService = Mock(BookQueryService)
    DailyStatCommandService dailyStatCommandService = Mock(DailyStatCommandService)

    void setup() {
        bookApplicationService = new BookApplicationService(bookQueryService, dailyStatCommandService)
    }

    def "search 메서드 호출시 검색 결과를 반환하면서 통계데이터를 저장한다."() {
        given:
        def givenQuery = "HTTP"
        def givenPage = 1
        def givenSize = 10

        when:
        bookApplicationService.search(givenQuery, givenPage, givenSize)

        then: "검색 결과를 조회할 때 인자가 조작되지 않는다."
        1 * bookQueryService.search(*_) >> {
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }

        and: "통계 데이터가 저장된다."
        1 * dailyStatCommandService.save(*_) >> {
            DailyStat dailyStat ->
                assert dailyStat.query == givenQuery
        }
    }
}
