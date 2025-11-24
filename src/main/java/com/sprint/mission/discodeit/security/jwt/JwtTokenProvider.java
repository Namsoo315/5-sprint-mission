package com.sprint.mission.discodeit.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {
  // 토큰을 발급, 갱신, 유효성 검사를 담당하는 컴포넌트

  // 브라우저에 내려줄 리프레시 토큰 쿠키 이름
  public static final String REFRESH_TOKEN_COOKIE_NAME = "DISCODEIT_REFRESH_TOKEN";

  private final long accessTokenExpirationMs;
  private final long refreshTokenExpirationMs;

  private final String issuer;

  private final JWSSigner accessTokenSigner;
  private final JWSVerifier accessTokenVerifier;
  private final JWSSigner refreshTokenSigner;
  private final JWSVerifier refreshTokenVerifier;

  public JwtTokenProvider(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.access-token-validity-seconds}") long accessTokenValiditySeconds,
      @Value("${security.jwt.refresh-token-validity-seconds}") long refreshTokenValiditySeconds,
      @Value("${security.jwt.issuer}") String issuer
  ) throws JOSEException {
    this.issuer = issuer;
    this.accessTokenExpirationMs = accessTokenValiditySeconds * 1000L;
    this.refreshTokenExpirationMs = refreshTokenValiditySeconds * 1000L;

    byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);

    this.accessTokenSigner = new MACSigner(secretBytes);
    this.accessTokenVerifier = new MACVerifier(secretBytes);
    this.refreshTokenSigner = new MACSigner(secretBytes);
    this.refreshTokenVerifier = new MACVerifier(secretBytes);
  }

  // ==========================================
  // ================ 토큰 생성 ================
  // ==========================================
  public String generateAccessToken(DiscodeitUserDetails discodeitUserDetails)
      throws JOSEException {
    return generateToken(discodeitUserDetails, accessTokenExpirationMs, accessTokenSigner,
        "access");
  }

  public String generateRefreshToken(DiscodeitUserDetails discodeitUserDetails)
      throws JOSEException {
    return generateToken(discodeitUserDetails, refreshTokenExpirationMs, refreshTokenSigner,
        "refresh");
  }

  private String generateToken(
      DiscodeitUserDetails discodeitUserDetails,
      long expirationMs,
      JWSSigner signer,
      String tokenType
  ) throws JOSEException {
    String tokenId = UUID.randomUUID().toString();
    UserDTO user = discodeitUserDetails.getUserDTO();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationMs);

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(user.username())
        .jwtID(tokenId)
        .issuer(issuer)
        .claim("userId", user.id().toString())
        .claim("type", tokenType)
        .claim("email", user.email())
        .claim("roles", discodeitUserDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()))
        .issueTime(now)
        .expirationTime(expiryDate)
        .build();

    SignedJWT signedJWT = new SignedJWT(
        new JWSHeader(JWSAlgorithm.HS256),
        claimsSet
    );

    signedJWT.sign(signer);
    String token = signedJWT.serialize();

    log.debug("Generated {} token for user: {}", tokenType, user.username());
    return token;
  }

  // ==========================================
  // ================ 토큰 검증 ================
  // ==========================================
  public boolean validateAccessToken(String token) {
    return validateToken(token, accessTokenVerifier, "access");
  }

  public boolean validateRefreshToken(String token) {
    return validateToken(token, refreshTokenVerifier, "refresh");
  }

  private boolean validateToken(String token, JWSVerifier verifier, String expectedType) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);

      // 1) 서명 검증
      if (!signedJWT.verify(verifier)) {
        log.debug("JWT signature verification failed for {} token", expectedType);
        return false;
      }

      // 2) type 클레임 검증
      String tokenType = (String) signedJWT.getJWTClaimsSet().getClaim("type");
      if (!expectedType.equals(tokenType)) {
        log.debug("JWT token type mismatch: expected {}, got {}", expectedType, tokenType);
        return false;
      }

      // 3) 만료 시간
      Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
      if (expirationTime == null || expirationTime.before(new Date())) {
        log.debug("JWT {} token expired", expectedType);
        return false;
      }

      return true;
    } catch (Exception e) {
      log.debug("JWT {} token validation failed: {}", expectedType, e.getMessage());
      return false;
    }
  }

  // 생성된 Refresh  토큰을 쿠키로 변환하는 과정
  public Cookie genereateRefreshTokenCookie(String refreshToken) {
    Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken); // 키쌍으로 만들어짐
    cookie.setHttpOnly(true); // 브라우저에서 읽기 금지
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge((int) (refreshTokenExpirationMs / 1000L)); // 쿠키 저장시간
    return cookie;
  }

  // 생성된 Refresh 무효화 하는 쿠키로 만드는 과정
  public Cookie genereateRefreshTokenExpirationCookie() {
    Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, ""); // 값 지우기
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0); // 무효화 시간 정하기
    return cookie;
  }

  // username 파싱
  public String getUsernameFromToken(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      return signedJWT.getJWTClaimsSet().getSubject();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid JWT token", e);
    }
  }

  // ==========================================
  // ================ JWT 파싱 ================
  // ==========================================
  public JwtObject parseAccessToken(String token) {
    return parseInternal(token, "access");
  }

  public JwtObject parseRefreshToken(String token) {
    return parseInternal(token, "refresh");
  }

  private JwtObject parseInternal(String token, String expectedType) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

      // 1) 서명 검증
      JWSVerifier verifier = "access".equals(expectedType)
          ? accessTokenVerifier
          : refreshTokenVerifier;

      if (!signedJWT.verify(verifier)) {
        throw new IllegalArgumentException("JWT 서명 검증 실패");
      }

      // 2) type 검증
      String actualType = claims.getStringClaim("type");
      if (!expectedType.equals(actualType)) {
        throw new IllegalArgumentException("JWT 타입 불일치: expected="
            + expectedType + ", actual=" + actualType);
      }

      // 3) 만료 체크
      Date exp = claims.getExpirationTime();
      if (exp == null || exp.before(new Date())) {
        throw new IllegalArgumentException("JWT 만료됨");
      }

      // 4) 생성 시 넣은 클레임 읽기
      UUID userId = UUID.fromString(claims.getStringClaim("userId"));
      String username = claims.getSubject();             // sub
      String email = claims.getStringClaim("email");
      Date issueTime = claims.getIssueTime();            // iat

      List<String> roleList = claims.getStringListClaim("roles");
      UserRole primaryRole = UserRole.valueOf(roleList.get(0).substring(5));

      UserDTO userDto = new UserDTO(
          userId,
          username,
          email,
          primaryRole,
          null,
          null
      );

      return new JwtObject(
          issueTime.toInstant(),
          exp.toInstant(),
          userDto,
          token
      );

    } catch (Exception e) {
      throw new IllegalArgumentException("JWT 파싱 실패: " + e.getMessage(), e);
    }
  }
}
