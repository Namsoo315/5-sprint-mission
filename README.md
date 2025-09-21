# 📌 Discodeit 프로젝트 마일스톤 및 요구사항

## 🚀 프로젝트 마일스톤

* **GURU**
* **로그 관리**
* **커스텀 예외 설계**
* **유효성 검사**
* **Actuator를 활용한 모니터링**
* **단위 테스트**
* **슬라이스 테스트**
* **통합 테스트**

---

## ✅ 기본 요구사항

### 프로파일 기반 설정 관리

* [ ] 개발(`dev`), 운영(`prod`) 환경에 대한 프로파일을 구성하세요.
* [ ] `application-dev.yaml`, `application-prod.yaml` 파일을 생성하세요.
* [ ] 데이터베이스 연결 정보, 서버 포트를 프로파일별로 분리하세요.

---

### 로그 관리

* [ ] Lombok의 `@Slf4j` 어노테이션을 활용해 로깅을 구성하세요.
* [ ] `application.yaml`에 기본 로깅 레벨(`info`)을 설정하세요.
* [ ] 환경별 로깅 레벨:

    * 개발 환경: `debug`
    * 운영 환경: `info`
* [ ] `logback-spring.xml` 파일을 생성하고, 로그 패턴과 출력 방식을 커스터마이징하세요.

**로그 패턴 예시**

```
{년}-{월}-{일} {시}:{분}:{초}:{밀리초} [{스레드명}] {로그 레벨(5글자)} {로거 이름(최대 36글자)} - {로그 메시지}{줄바꿈}
```

**출력 예시**

```
25-01-01 10:33:55.740 [main] DEBUG c.s.m.discodeit.DiscodeitApplication - Running with Spring Boot v3.4.0, Spring v6.2.0
```

* [ ] 콘솔과 파일에 동시에 로그 기록
* [ ] 로그 파일 저장 경로: `{프로젝트 루트}/.logs`
* [ ] 로그 파일은 **일자별 롤링** 및 **30일 보관**
* [ ] 서비스/컨트롤러 주요 메소드에 로깅 추가

    * 사용자 생성/수정/삭제
    * 채널 생성/수정/삭제
    * 메시지 생성/수정/삭제
    * 파일 업로드/다운로드

---

### 예외 처리 고도화

* [ ] 패키지: `com.sprint.mission.discodeit.exception[.{도메인}]`
* [ ] `ErrorCode` Enum 클래스를 통해 예외 코드와 메시지 정의
* [ ] 기본 예외 클래스: `DiscodeitException`
* [ ] 도메인별 예외 클래스 정의 (`UserException`, `ChannelException` 등)
* [ ] 구체 예외 정의 (`UserNotFoundException`, `UserAlreadyExistException` 등)
* [ ] 기존 표준 예외 (`NoSuchElementException`, `IllegalArgumentException`) → 커스텀 예외로 대체
* [ ] 일관된 예외 응답(`ErrorResponse`) 설계
* [ ] `@RestControllerAdvice` 기반 예외 핸들러 구현

---

### 유효성 검사

* [ ] Spring Validation 의존성 추가
* [ ] Request DTO에 제약 조건 어노테이션 적용 (`@NotNull`, `@NotBlank`, `@Size`, `@Email` 등)
* [ ] 컨트롤러에서 `@Valid` 활용
* [ ] 유효성 검증 실패 시 `MethodArgumentNotValidException` 처리
* [ ] 상세 오류 메시지를 포함한 응답 반환

---

### Actuator

* [ ] Spring Boot Actuator 의존성 추가
* [ ] 기본 엔드포인트 활성화: `health`, `info`, `metrics`, `loggers`
* [ ] 애플리케이션 정보 추가 (`info` 엔드포인트)

    * 이름: `Discodeit`
    * 버전: `1.7.0`
    * Java: 17
    * Spring Boot: 3.4.0
    * 주요 설정 정보 (DB, JPA, storage, multipart 등)
* [ ] 서버 실행 후 `/actuator/*` 엔드포인트 확인

---

### 단위 테스트

* [ ] 서비스 레이어 단위 테스트 작성
* [ ] 각 서비스별 최소 2개 이상(성공/실패) 케이스

    * UserService: `create`, `update`, `delete`
    * ChannelService: `create`, `update`, `delete`, `findByUserId`
    * MessageService: `create`, `update`, `delete`, `findByChannelId`
* [ ] `Mockito`, `BDDMockito` 활용

---

### 슬라이스 테스트

* [ ] 레포지토리 레이어 → `@DataJpaTest` 활용
* [ ] `application-test.yaml` 생성 (H2 인메모리 DB, PostgreSQL 호환 모드)
* [ ] 테스트 시 스키마 새로 생성
* [ ] User, Channel, Message 주요 쿼리 메소드 테스트 (성공/실패)
* [ ] 컨트롤러 레이어 → `@WebMvcTest` 활용
* [ ] 필요시 `@Import`로 Bean 등록
* [ ] MockMvc 활용 JSON 응답 검증

---

### 통합 테스트

* [ ] `@SpringBootTest` 기반 통합 테스트 구성
* [ ] H2 인메모리 DB 사용
* [ ] 주요 API 엔드포인트 테스트 (성공/실패)

    * 사용자: 생성, 수정, 삭제, 목록 조회
    * 채널: 생성, 수정, 삭제
    * 메시지: 생성, 수정, 삭제, 목록 조회
* [ ] 각 테스트는 `@Transactional`로 독립 실행

---

## 🔥 심화 요구사항

### MDC 기반 로깅 고도화

* [ ] `MDCLoggingInterceptor` 구현 (패키지: `com.**.discodeit.config`)
* [ ] 요청 ID(UUID), 요청 URL, 요청 방식 추가
* [ ] 응답 헤더에 `Discodeit-Request-ID` 포함
* [ ] `WebMvcConfig` 통해 인터셉터 등록
* [ ] Logback 패턴에 MDC 값 추가

**패턴 예시**

```
{년}-{월}-{일} {시}:{분}:{초}:{밀리초} [{스레드명}] {로그 레벨(5글자)} {로거 이름(최대 36글자)} [{MDC:요청ID} | {MDC:요청 메소드} | {MDC:요청 URL}] - {로그 메시지}{줄바꿈}
```

---

### Spring Boot Admin 메트릭 가시화

* [ ] Spring Boot Admin 서버 모듈 생성 (포트: 9090)
* [ ] `@EnableAdminServer` 적용
* [ ] Client 설정: `spring.boot.admin.client.url`
* [ ] Admin 대시보드에서 인스턴스 등록 및 메트릭 확인

---

### 테스트 커버리지 관리

* [ ] JaCoCo 플러그인 추가
* [ ] 테스트 실행 후 HTML, XML 리포트 생성 (`build/reports/jacoco`)
* [ ] `com.sprint.mission.discodeit.service.basic` 패키지 기준 **60% 이상 커버리지** 달성

---

👉 위 요구사항을 기반으로 단계별 기능 구현 및 검증을 진행합니다.