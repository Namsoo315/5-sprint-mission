package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.exception.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponse;
import com.sprint.mission.discodeit.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	// [ ] 사용자는 로그인할 수 있다.
	@RequestMapping(path = "/", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse<AuthLoginResponse>> login(@RequestBody AuthLoginRequest request) {
		AuthLoginResponse response = authService.login(request);

		return ResponseEntity.ok(ApiResponse.ok(response, "로그인에 성공하였습니다."));
	}
}
