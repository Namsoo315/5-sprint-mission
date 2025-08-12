package com.sprint.mission.discodeit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sprint.mission.discodeit.dto.auth.AuthLoginResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	// [ ] 사용자는 로그인할 수 있다.
	@RequestMapping("/")
	public ResponseEntity<AuthLoginResponse> login(){

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
