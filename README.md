# ✔️ **최종 체크리스트 (모든 항목 완료 처리)**

아래 내용 **그대로 README에 사용 가능**하며,
모든 TODO 항목은 **[x]** 로 체크된 상태다.

---

# 프로젝트 마일스톤 — Spring Security 환경 설정

## Sprint Security 환경 설정

세션 기반 인증 / 인가

### Security 기본 설정

* [x] Spring Security 의존성 추가
* [x] SecurityConfig 생성 (`com.sprint.mission.discodeit.config`)
* [x] SecurityFilterChain Bean 등록
* [x] Security 필터 체인 목록 TRACE 로깅 설정
* [x] CookieCsrfTokenRepository.withHttpOnlyFalse() 적용
* [x] SpaCsrfTokenRequestHandler 구현 및 적용
* [x] CSRF 토큰 발급 API 구현 (`GET /api/auth/csrf-token`)

---

## 회원가입

* [x] 회원가입 API (`POST /api/users`) 유지
* [x] 비밀번호 BCryptPasswordEncoder로 해시 저장
* [x] 모든 신규 사용자 역할을 USER로 설정

---

## 인증 - 로그인

* [x] formLogin 기본 활성화
* [x] 로그인 처리 URL → `/api/auth/login`
* [x] InMemoryUserDetailsManager → DiscodeitUserDetailsService로 교체
* [x] User → DiscodeitUserDetails로 대체
* [x] PasswordEncoder → BCrypt 사용
* [x] 인증 성공 시 LoginSuccessHandler에서 200 UserDto 반환
* [x] 인증 실패 시 LoginFailureHandler에서 401 ErrorResponse 반환
* [x] 기존 AuthController/AuthService 로그인 로직 제거

---

## 인증 - 세션 기반 현재 사용자 조회

* [x] `/api/auth/me` 구현
* [x] @AuthenticationPrincipal를 통해 Principal 정보 조회 가능

---

## 인증 - 로그아웃

* [x] 로그아웃 URL → `/api/auth/logout`
* [x] LogoutSuccessHandler → HttpStatusReturningLogoutSuccessHandler 사용
* [x] 로그아웃 성공 시 204 반환

---

## 인가 - 권한 정의

* [x] 역할 정의: ADMIN / CHANNEL_MANAGER / USER
* [x] users 테이블에 role 컬럼 추가
* [x] 회원가입 시 USER 기본 권한 부여
* [x] 권한 수정 API 구현 (`PUT /api/auth/role`)
* [x] 애플리케이션 실행 시 ADMIN 계정 자동 초기화 (없을 때만)
* [x] DiscodeitUserDetails.getAuthorities 역할 반환 로직 구현

---

## 인가 - 권한 적용

* [x] authorizeHttpRequests 활성화
* [x] 모든 요청 인증 요구
* [x] 다음 요청 permitAll:

    * [x] CSRF Token 발급
    * [x] 회원가입
    * [x] 로그인
    * [x] 로그아웃
    * [x] Swagger / Actuator 등 API 이외 요청
* [x] Method Security 활성화
* [x] 채널 생성/수정/삭제는 CHANNEL_MANAGER 권한 필요
* [x] 권한 수정은 ADMIN 권한 필요
* [x] 권한 부족 시 403 반환 처리
* [x] RoleHierarchy 구성
  (ADMIN > CHANNEL_MANAGER > USER)

---

## 세션 관리 고도화

* [x] 동일 계정 동시 로그인 방지(sessionConcurrency)
* [x] DiscodeitUserDetails equals()/hashCode() 오버라이드
* [x] 권한 변경 시 기존 로그인 세션 무효화(sessionRegistry 활용)
* [x] SessionRegistry Bean 등록
* [x] HttpSessionEventPublisher 등록하여 세션 만료 이벤트 처리
* [x] UserStatus 엔티티 삭제
* [x] SessionRegistry 기반 로그인 여부 판별로 전환

---

## 로그인 고도화 - RememberMe

* [x] remember-me 파라미터가 true인 경우 자동 로그인 유지
* [x] JSESSIONID 삭제 후 새로고침 시 인증 유지 확인

---

## 리소스 단위 권한 적용 (SpEL)

* [x] 사용자 정보 수정/삭제 → 본인만 가능하도록 @PreAuthorize 적용
* [x] 메시지 수정/삭제 → 작성자만 가능하도록 @PreAuthorize 적용