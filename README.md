# ✅ **리팩토링 README (완성본)**

# JWT 컴포넌트 구현

- [x]  JWT 의존성을 추가하세요.

```gradle
implementation 'com.nimbusds:nimbus-jose-jwt:10.3'
````

- [x]  토큰을 발급, 갱신, 유효성 검사를 담당하는 컴포넌트(JwtTokenProvider)를 구현하세요.

---

# 리팩토링 - 로그인

미션 9와 마찬가지로 Spring Security의 formLogin + 미션 9의 인증 흐름은 그대로 유지하면서 필요한 부분만 대체합니다.

- [x]  세션 생성 정책을 STATELESS로 변경하고, sessionConcurrency 설정을 삭제하세요.

```java
http
    .sessionManagement(session ->session
    .

sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );
```

- [x]  AuthenticationSuccessHandler 컴포넌트를 대체하세요.

기존 구현체는 `LoginSuccessHandler`입니다.
`JwtLoginSuccessHandler`를 정의하고 대체하세요.

```java

@Component
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    JwtDto jwt = jwtTokenProvider.createToken(authentication);

    // access token → body
    response.setContentType("application/json");
    response.getWriter().write(new ObjectMapper().writeValueAsString(jwt));

    // refresh token → cookie
    Cookie cookie = new Cookie(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME, jwt.refreshToken());
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    response.addCookie(cookie);
  }
}
```

설정에 추가하세요.

```java
http
    .formLogin(login ->login
    .

successHandler(jwtLoginSuccessHandler)
    );
```

---

# JWT 인증 필터 구현

- [x]  엑세스 토큰을 통해 인증하는 필터(JwtAuthenticationFilter)를 구현하세요.

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtRegistry jwtRegistry;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String token = jwtTokenProvider.resolveAccessToken(request);

    if (token != null && jwtTokenProvider.validateToken(token)) {

      if (!jwtRegistry.hasActiveJwtInformationByAccessToken(token)) {
        filterChain.doFilter(request, response);
        return;
      }

      UserDetails userDetails = jwtTokenProvider.getUserDetails(token);

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}
```

---

# 리프레시 토큰을 활용한 엑세스 토큰 재발급

- [x]  리프레시 토큰을 활용해 엑세스 토큰을 재발급하는 API를 구현하세요.

**엔드포인트:** `POST /api/auth/refresh`

```java

@PostMapping("/refresh")
public ResponseEntity<JwtDto> refreshToken(HttpServletRequest request) {

  String refreshToken = jwtTokenProvider.resolveRefreshTokenFromCookie(request);

  if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  JwtDto newTokens = jwtTokenProvider.rotateToken(refreshToken);

  return ResponseEntity.ok(newTokens);
}
```

- [x]  리프레시 토큰 Rotation 적용
- [x]  Me API 삭제
- [x]  RememberMe 기능 제거

---

# 리팩토링 - 로그아웃

- [x]  쿠키의 리프레시 토큰을 삭제하는 LogoutHandler 구현

```java

@Component
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtRegistry jwtRegistry;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    Cookie[] cookies = request.getCookies();

    if (cookies == null)
      return;

    Arrays.stream(cookies)
        .filter(c -> c.getName().equals(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME))
        .findFirst()
        .ifPresent(cookie -> {
          jwtRegistry.invalidateJwtInformationByRefreshToken(cookie.getValue());

          cookie.setMaxAge(0);
          cookie.setPath("/");
          response.addCookie(cookie);
        });
  }
}
```

- [x]  설정에 추가

```java
http
    .logout(logout ->logout
    .

logoutUrl("/api/auth/logout")
        .

addLogoutHandler(jwtLogoutHandler)
    );
```

---

# 리팩토링 - 토큰 상태 관리

토큰 기반 인증 방식은 세션 기반 인증 방식과 달리 무상태(stateless)이기 때문에,
세션처럼 사용자의 로그인 상태를 제어하기 어려움 → **JwtRegistry 필요**

---

## JwtRegistry 구현

- [x]  InMemoryJwtRegistry 구현

```java
public class InMemoryJwtRegistry implements JwtRegistry {

  private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
  private final int maxActiveJwtCount = 1;

  @Override
  public synchronized void registerJwtInformation(JwtInformation info) {
    origin.computeIfAbsent(info.userId(), id -> new ConcurrentLinkedQueue<>());

    Queue<JwtInformation> queue = origin.get(info.userId());

    while (queue.size() >= maxActiveJwtCount) {
      JwtInformation old = queue.poll();
      removeTokenIndex(old.accessToken(), old.refreshToken());
    }

    queue.add(info);
    indexTokens(info);
  }

  @Override
  public void invalidateJwtInformationByUserId(UUID userId) {
    origin.computeIfPresent(userId, (id, queue) -> {
      queue.forEach(info -> removeTokenIndex(info.accessToken(), info.refreshToken()));
      queue.clear();
      return null;
    });
  }

  @Override
  public void clearExpiredJwtInformation() {
    // expiration 제거 로직
  }
}
```

---

- [x]  JwtAuthenticationFilter에서 JwtRegistry 활용
- [x]  동시 로그인 제한 로직 구현
- [x]  권한 변경 시 강제 로그아웃 처리
- [x]  로그인 여부 판단에 JwtRegistry 활용
- [x]  JwtLogoutHandler에서 JwtRegistry 연동
- [x]  스케줄링 활성화

```java

@Configuration
@EnableJpaAuditing
@EnableScheduling
public class AppConfig {

}
```

```java

@Scheduled(fixedDelay = 1000 * 60 * 5)
@Override
public void clearExpiredJwtInformation() {
    ...
}
```

---
## 멘토에게
- 
