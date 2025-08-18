package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 전역으로 예외처리를 할 수 있어야 한다.
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		//일단 간단한 글로벌 예외 처리
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
}
