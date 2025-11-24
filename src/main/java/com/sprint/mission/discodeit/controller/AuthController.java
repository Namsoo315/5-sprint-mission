package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.JwtDTO;
import com.sprint.mission.discodeit.dto.data.JwtInformation;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "로그인 인증 관련 API")
@Slf4j
public class AuthController {

  private final AuthService authService;
  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;

  // [ ] 사용자는 로그인할 수 있다.
  @Operation(summary = "로그인 API", responses = {
      @ApiResponse(responseCode = "200", description = "로그인 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
      @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 올바르지 않습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping("/login")
  public ResponseEntity<UserDTO> login(
      @RequestBody @Valid AuthLoginRequest authLoginRequest) {

    UserDTO authLoginResponse = authService.login(authLoginRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(authLoginResponse);
  }

  @Operation(summary = "CSRF 토큰 발급 API", responses = {
      @ApiResponse(responseCode = "203", description = "토큰 발급 성공"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping("/csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {

    String token = csrfToken.getToken();

    log.debug("CSRF Token 요청 : {}", token);

    return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
        .body(null);
  }

  @Operation(summary = "JWT 토큰 재발급", responses = {
      @ApiResponse(responseCode = "200", description = "토큰 재발급 완료"),
      @ApiResponse(responseCode = "401", description = "토큰이 유효하지 않습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping("/refresh")
  public ResponseEntity<JwtDTO> refresh(
      @CookieValue(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
      HttpServletResponse response) {
    log.info("Refresh Token: {}", refreshToken);
    JwtInformation jwtInformation = authService.refreshToken(refreshToken);
    Cookie cookie = jwtTokenProvider.genereateRefreshTokenCookie(jwtInformation.getRefreshToken());
    response.addCookie(cookie);

    JwtDTO jwtDTO = new JwtDTO(jwtInformation.getUserDTO(), jwtInformation.getAccessToken());

    return ResponseEntity.status(HttpStatus.OK).body(jwtDTO);
  }

  @Operation(summary = "사용자 권한 수정", responses = {
      @ApiResponse(responseCode = "200", description = "권한 수정 완료"),
      @ApiResponse(responseCode = "401", description = "권한이 없습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PutMapping("/role")
  public ResponseEntity<UserDTO> updateRole(
      @RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {
    log.debug("사용자 권한 수정 : {}", userRoleUpdateRequest.newRole());

    UserDTO userDTO = userService.updateRoleUser(userRoleUpdateRequest);

    return ResponseEntity.status(HttpStatus.OK).body(userDTO);
  }

}
