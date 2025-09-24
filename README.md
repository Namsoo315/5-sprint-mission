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

* [x] 개발(`dev`), 운영(`prod`) 환경에 대한 프로파일을 구성하세요.
* [x] `application-dev.yaml`, `application-prod.yaml` 파일을 생성하세요.
* [x] 데이터베이스 연결 정보, 서버 포트를 프로파일별로 분리하세요.

---

### 로그 관리

* [x] Lombok의 `@Slf4j` 어노테이션을 활용해 로깅을 구성하세요.
* [x] `application.yaml`에 기본 로깅 레벨(`info`)을 설정하세요.
* [x] 환경별 로깅 레벨:

    * 개발 환경: `debug`
    * 운영 환경: `info`
* [x] `logback-spring.xml` 파일을 생성하고, 로그 패턴과 출력 방식을 커스터마이징하세요.

**로그 패턴 예시**

```
{년}-{월}-{일} {시}:{분}:{초}:{밀리초} [{스레드명}] {로그 레벨(5글자)} {로거 이름(최대 36글자)} - {로그 메시지}{줄바꿈}
```

**출력 예시**

```
25-01-01 10:33:55.740 [main] DEBUG c.s.m.discodeit.DiscodeitApplication - Running with Spring Boot v3.4.0, Spring v6.2.0
```

* [x] 콘솔과 파일에 동시에 로그 기록
* [x] 로그 파일 저장 경로: `{프로젝트 루트}/.logs`
* [x] 로그 파일은 **일자별 롤링** 및 **30일 보관**
* [x] 서비스/컨트롤러 주요 메소드에 로깅 추가

    * 사용자 생성/수정/삭제
    * 채널 생성/수정/삭제
    * 메시지 생성/수정/삭제
    * 파일 업로드/다운로드

---

### 예외 처리 고도화

* [x] 패키지: `com.sprint.mission.discodeit.exception[.{도메인}]`
* [x] `ErrorCode` Enum 클래스를 통해 예외 코드와 메시지 정의
* [x] 기본 예외 클래스: `DiscodeitException`
* [x] 도메인별 예외 클래스 정의 (`UserException`, `ChannelException` 등)
* [x] 구체 예외 정의 (`UserNotFoundException`, `UserAlreadyExistException` 등)
* [x] 기존 표준 예외 (`NoSuchElementException`, `IllegalArgumentException`) → 커스텀 예외로 대체
* [x] 일관된 예외 응답(`ErrorResponse`) 설계
* [x] `@RestControllerAdvice` 기반 예외 핸들러 구현

---

### 유효성 검사

* [x] Spring Validation 의존성 추가
* [x] Request DTO에 제약 조건 어노테이션 적용 (`@NotNull`, `@NotBlank`, `@Size`, `@Email` 등)
* [x] 컨트롤러에서 `@Valid` 활용
* [x] 유효성 검증 실패 시 `MethodArgumentNotValidException` 처리
* [x] 상세 오류 메시지를 포함한 응답 반환

---

### Actuator

* [x] Spring Boot Actuator 의존성 추가
* [x] 기본 엔드포인트 활성화: `health`, `info`, `metrics`, `loggers`
* [x] 애플리케이션 정보 추가 (`info` 엔드포인트)

    * 이름: `Discodeit`
    * 버전: `1.7.0`
    * Java: 17
    * Spring Boot: 3.4.0
    * 주요 설정 정보 (DB, JPA, storage, multipart 등)
* [x] 서버 실행 후 `/actuator/*` 엔드포인트 확인

---

### 단위 테스트

* [x] 서비스 레이어 단위 테스트 작성
* [x] 각 서비스별 최소 2개 이상(성공/실패) 케이스

    * UserService: `create`, `update`, `delete`
    * ChannelService: `create`, `update`, `delete`, `findByUserId`
    * MessageService: `create`, `update`, `delete`, `findByChannelId`
* [x] `Mockito`, `BDDMockito` 활용

---

### 슬라이스 테스트

* [x] 레포지토리 레이어 → `@DataJpaTest` 활용
* [x] `application-test.yaml` 생성 (H2 인메모리 DB, PostgreSQL 호환 모드)
* [x] 테스트 시 스키마 새로 생성
* [x] User, Channel, Message 주요 쿼리 메소드 테스트 (성공/실패)
* [x] 컨트롤러 레이어 → `@WebMvcTest` 활용
* [x] 필요시 `@Import`로 Bean 등록
* [x] MockMvc 활용 JSON 응답 검증

---

### 통합 테스트

* [x] `@SpringBootTest` 기반 통합 테스트 구성
* [x] H2 인메모리 DB 사용
* [x] 주요 API 엔드포인트 테스트 (성공/실패)

    * 사용자: 생성, 수정, 삭제, 목록 조회
    * 채널: 생성, 수정, 삭제
    * 메시지: 생성, 수정, 삭제, 목록 조회
* [x] 각 테스트는 `@Transactional`로 독립 실행

---

## 🔥 심화 요구사항

### MDC 기반 로깅 고도화

* [x] `MDCLoggingInterceptor` 구현 (패키지: `com.**.discodeit.config`)
* [x] 요청 ID(UUID), 요청 URL, 요청 방식 추가
* [x] 응답 헤더에 `Discodeit-Request-ID` 포함
* [x] `WebMvcConfig` 통해 인터셉터 등록
* [x] Logback 패턴에 MDC 값 추가

**패턴 예시**

```
{년}-{월}-{일} {시}:{분}:{초}:{밀리초} [{스레드명}] {로그 레벨(5글자)} {로거 이름(최대 36글자)} [{MDC:요청ID} | {MDC:요청 메소드} | {MDC:요청 URL}] - {로그 메시지}{줄바꿈}
```

---

### Spring Boot Admin 메트릭 가시화

* [x] Spring Boot Admin 서버 모듈 생성 (포트: 9090)
* [x] `@EnableAdminServer` 적용
* [x] Client 설정: `spring.boot.admin.client.url`
* [x] Admin 대시보드에서 인스턴스 등록 및 메트릭 확인

<img width="1612" height="367" alt="{B3EED0BF-31A3-4CB6-BF3B-7E1670FA4FA1}" src="https://github.com/user-attachments/assets/5b89befe-693c-402c-981b-4ecfffeb6fd4" />
<img width="1807" height="451" alt="{37C19C9B-B14A-41C9-88AF-F7F2BAE364DB}" src="https://github.com/user-attachments/assets/2ce5e68b-85ec-4da7-b2ce-6e25f6198aa9" />

---

### 테스트 커버리지 관리

* [x] JaCoCo 플러그인 추가
* [x] 테스트 실행 후 HTML, XML 리포트 생성 (`build/reports/jacoco`)
* [x] `com.sprint.mission.discodeit.service.basic` 패키지 기준 **60% 이상 커버리지** 달성
<img width="1244" height="319" alt="{744D81D5-6885-40EE-87EF-3F39E1C00A2B}" src="https://github.com/user-attachments/assets/1c9cec78-1862-4543-9c13-7935270ec0de" />

<img width="1078" height="566" alt="{16D26FD4-C833-4840-9F95-DCB7AECC5E0F}" src="https://github.com/user-attachments/assets/51349dc8-7abf-4d4e-a902-71c18e5ba6ed" />

내가 만든 서비스의 모든 조건을 충족시켜야 하나?

---

👉 위 요구사항을 기반으로 단계별 기능 구현 및 검증을 진행합니다.
