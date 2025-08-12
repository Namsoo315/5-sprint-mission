package com.sprint.mission.discodeit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	//[ ] 사용자를 등록할 수 있다.
	@RequestMapping("/")
	public ResponseEntity<User> registerUser (@RequestBody User user) {

		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	// [ ] 사용자 정보를 수정할 수 있다.
	@RequestMapping("/modify")
	public ResponseEntity<User> modifyUser (@RequestBody User user) {
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	// [ ] 사용자를 삭제할 수 있다.
	@RequestMapping("/delete")
	public ResponseEntity<User> deleteUser (@RequestBody User user) {
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	// [ ] 모든 사용자를 조회할 수 있다.
	@RequestMapping("/findAll")
	public ResponseEntity<List<User>> findAllUser () {
		List<User> users = new ArrayList<>();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	// [ ] 사용자의 온라인 상태를 업데이트할 수 있다.
	@RequestMapping("/update")
	public ResponseEntity<User> updateUser (@RequestBody User user) {
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	// [ ]  웹 API의 예외를 전역으로 처리하세요.
	// 공통 예외 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<User> handleException(Exception e) {
		e.printStackTrace();		// 임시
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
