package com.sprint.mission.discodeit.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sprint.mission.discodeit.entity.Message;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

	// [ ] 메시지를 보낼 수 있다.
	@RequestMapping("/")
	public ResponseEntity<Message> sendMessage() {

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	// [ ] 메시지를 수정할 수 있다.
	@RequestMapping("/modify")
	public ResponseEntity<Message> modifyMessage() {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	// [ ] 메시지를 삭제할 수 있다.
	@RequestMapping("/delete")
	public ResponseEntity<Message> deleteMessage() {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	// [ ] 특정 채널의 메시지 목록을 조회할 수 있다.
	@RequestMapping("/id")
	public ResponseEntity<Message> findMessageById(@RequestParam("id") UUID userId) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
