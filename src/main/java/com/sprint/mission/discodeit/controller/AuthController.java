package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;

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
	public ResponseEntity<ApiResponse<AuthLoginResponse>> login(@RequestBody AuthLoginRequest authLoginRequest) {
		AuthLoginResponse authLoginResponse = authService.login(authLoginRequest);

		return ResponseEntity.ok(ApiResponse.ok(authLoginResponse, "로그인에 성공하였습니다."));
	}
}
