package com.sprint.mission.discodeit.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sprint.mission.discodeit.entity.Message;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/readStatus")
public class ReadStatusController {
	// [ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
	@RequestMapping("/")
	public ResponseEntity<Message> CreateReadStatus() {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	// [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
	@RequestMapping("/modify")
	public ResponseEntity<Message> ModifyReadStatus() {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	// [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
	@RequestMapping("/{id}")	//특정 사용자가 누구인가?
	public ResponseEntity<Message> findReadStatusById(@PathVariable("id") UUID userId) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
