package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.response.AuthLoginResponse;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "로그인 인증 관련 API")
public class AuthController {

  private final AuthService authService;

  // [ ] 사용자는 로그인할 수 있다.
  @Operation(summary = "로그인 API", responses = {
      @ApiResponse(responseCode = "200", description = "로그인 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
      @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 올바르지 않습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping("/login")
  public ResponseEntity<AuthLoginResponse> login(
      @RequestBody AuthLoginRequest authLoginRequest) {
    AuthLoginResponse authLoginResponse = authService.login(authLoginRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(authLoginResponse);
  }
}
