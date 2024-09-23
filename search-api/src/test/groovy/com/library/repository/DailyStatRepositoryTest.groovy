package com.library.repository

import com.library.entity.DailyStat
import com.library.feign.NaverClient
import jakarta.persistence.EntityManager
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDateTime

@ActiveProfiles("test")
@DataJpaTest
class DailyStatRepositoryTest extends Specification {

    @Autowired
    DailyStatRepository dailyStatRepository

    // 실제 쿼리가 날라가는지 확인하기 위해 em 으로 영속성 컨텍스트를 클리어해준다.
    @Autowired
    EntityManager em

    // 외부 시스템과 통신을 대체하고 싶을 때 사용
    @SpringBean
    NaverClient naverClient = Mock()

    def "저장 후 조회가 된다."() {
        given:
        def givenQuery = "HTTP"

        when:
        def dailyStat = new DailyStat(givenQuery, LocalDateTime.now())
        def saved = dailyStatRepository.saveAndFlush(dailyStat)

        then: "실제 저장이 된다."
        saved.id != null

        when: "저장 후 조회"
        // 실제 쿼리 동작을 확인해야 하기 때문에 영속성 컨텍스트 클리어
        em.clear()
        def result = dailyStatRepository.findById(saved.id)

        then: "영속성 컨텍스트를 비웠으니 캐시가 아닌 실제 DB 쿼리로 데이터를 가져온다."
        verifyAll {
            result.isPresent()
            result.get().query == givenQuery
        }
    }
}
