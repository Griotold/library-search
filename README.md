# 프로젝트

- Library Search API
  이 프로젝트는 사용자가 도서 제목으로 도서를 검색하고, 검색 결과를 페이징 형태로 제공하는 도서 검색 시스템입니다. 또한, 사용자들이 검색한 도서에 대한 통계를 제공하여, 인기 검색어와 일별 검색 횟수를 분석할
  수 있습니다.

# api docs

http://localhost:8080/swagger-ui/index.html

# 외부 연동 API 출처

- https://openapi.naver.com
- https://developers.kakao.com

# 기능 요구사항

- 도서 검색 기능

    - 도서 제목으로 도서를 검색하여 도서 정보를 제공해야 한다.

    - 검색 결과는 페이징 형태로 제공되어야 한다.

- 도서 검색 통계 기능

    - 사용자들이 검색한 도서에 대한 검색어 횟수를 일별로 제공해야한다.

    - 사용자들이 검색한 도서 순위를 상위 5개를 제공해야한다.

# 작업 목록

EPIC: LS-0

- LS-1: 규모추정 & 시스템디자인

- LS-2: 요구사항 작성

- LS-3: 멀티모듈 구성

- LS-4: 외부 api 연동

- LS-5: api 서버 구현

- LS-6: 데이터베이스 연결

- LS-7: 검색 통계 기능 구현

- LS-8: 문서화

- LS-9: 고가용성을 위한 설계

- LS-10: event

# 시스템디자인(AS_IS)

![시스템디자인_AS_IS](https://github.com/user-attachments/assets/0e254552-fc6b-4a3b-8683-5709291adc41)

# 시스템디자인(TO_BE)

![시스템디자인_TO_BE](https://github.com/user-attachments/assets/fc612f3f-6ab1-4551-8658-4d58c1f37bbc)

개선포인트

- External Server 별도 구성
- 외부 api 일 호출량 제한을 대비한 다수 키 매니징 기능 구현
- Event발행 별도 메세지 서버 구성
- RDS에 정해진 처리량만큼 write하는 worker 서버 구성
- 데이터베이스 일간 파티셔닝