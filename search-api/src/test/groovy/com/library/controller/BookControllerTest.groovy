package com.library.controller

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.http.HttpStatus

import com.library.service.BookQueryService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

class BookControllerTest extends Specification {
    BookQueryService bookQueryService = Mock(BookQueryService)

    BookController bookController
    MockMvc mockMvc

    void setup() {
        bookController = new BookController(bookQueryService)
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build()
    }

    def "search"() {
        given:
        def givenQuery = "HTTP"
        def givenPage = 1
        def givenSize = 10

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/books?query=${givenQuery}&page=${givenPage}&size=${givenSize}"))
                .andReturn()
                .response

        then:
        response.status == HttpStatus.OK.value()

        and:         // 넘겨주는 인자 검증까지!
        1 * bookQueryService.search(*_) >> {
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }
    }
}
