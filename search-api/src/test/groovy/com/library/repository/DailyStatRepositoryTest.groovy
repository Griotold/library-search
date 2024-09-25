package com.library.repository


import com.library.entity.DailyStat
import com.library.feign.NaverClient
import jakarta.persistence.EntityManager
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
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

    def "쿼리의 카운트를 조회한다."() {
        given:
        def givenQuery = "HTTP"
        def now = LocalDateTime.of(2024, 5, 2, 0, 0, 0)
        // 검색될 레코드들
        def stat1 = new DailyStat(givenQuery, now.plusMinutes(10))
        def stat2 = new DailyStat(givenQuery, now.plusMinutes(20))

        // 검색되지 않아야할 레코드들
        def stat3 = new DailyStat(givenQuery, now.minusMinutes(1))
        def stat4 = new DailyStat("JAVA", now.plusMinutes(10))

        // dailyStatRepository.saveAll([stat1, stat2, stat3, stat4])
        def stats = [stat1, stat2, stat3, stat4]
        // groovy 에서는 메소드 호출시 괄호가 필요 없다고 함.
        // 따라서 "dailyStatRepository.saveAll stats" 도 가능함.
        dailyStatRepository.saveAll(stats)

        when: "오늘부터 내일까지 검색된 게 몇개인지 조회"
        def result = dailyStatRepository.countByQueryAndEventDateTimeBetween(givenQuery, now, now.plusDays(1))

        then: "2개가 나와야 한다."
        result == 2
    }

    def "가장 많이 검색된 쿼리 키워드를 개수와 함께 상위 3개 반환한다."() {
        given:
        def now = LocalDateTime.now()
        def stat1 = new DailyStat('HTTP', now.plusMinutes(10))
        def stat2 = new DailyStat('HTTP', now.plusMinutes(10))
        def stat3 = new DailyStat('HTTP', now.plusMinutes(10))
        def stat4 = new DailyStat('JAVA', now.plusMinutes(10))
        def stat5 = new DailyStat('JAVA', now.plusMinutes(10))
        def stat6 = new DailyStat('JAVA', now.plusMinutes(10))
        def stat7 = new DailyStat('JAVA', now.plusMinutes(10))
        def stat8 = new DailyStat('SPRING', now.plusMinutes(10))
        def stat9 = new DailyStat('SPRING', now.plusMinutes(10))
        def stat10 = new DailyStat('OS', now.plusMinutes(10))

        dailyStatRepository.saveAll([stat1, stat2, stat3, stat4, stat5, stat6, stat7, stat8, stat9, stat10])

        when:
        def request = PageRequest.of(0, 3)
        def response = dailyStatRepository.findTopQuery(request)

        then:
        verifyAll {
            response.size() == 3
            // query, query() 모두 가능 -> 둘 다 getter 호출
            response[0].query == 'JAVA'
            response[0].count() == 4
            response[1].query() == 'HTTP'
            response[1].count() == 3
            response[2].query() == 'SPRING'
            response[2].count() == 2
        }

    }
}
