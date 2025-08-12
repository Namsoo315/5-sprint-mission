package com.sprint.mission.discodeit.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sprint.mission.discodeit.entity.Channel;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {

	// [ ] 공개 채널을 생성할 수 있다.
	@RequestMapping("/public")
	public ResponseEntity<Channel> publicChannel(@RequestBody Channel channel) {

		return ResponseEntity.status(HttpStatus.CREATED).body(channel);
	}

	// [ ] 비공개 채널을 생성할 수 있다.
	@RequestMapping("/private")
	public ResponseEntity<Channel> privateChannel(@RequestBody Channel channel) {
		return ResponseEntity.status(HttpStatus.OK).body(channel);
	}

	// [ ] 공개 채널의 정보를 수정할 수 있다.
	@RequestMapping("/public/modify")
	public ResponseEntity<Channel> modifyPublicChannel(@RequestBody Channel channel) {
		return ResponseEntity.status(HttpStatus.OK).body(channel);
	}

	// [ ] 채널을 삭제할 수 있다.
	@RequestMapping("/delete")
	public ResponseEntity<Channel> deleteChannel(@RequestBody Channel channel) {
		return ResponseEntity.status(HttpStatus.OK).body(channel);
	}

	// [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
	@RequestMapping("/{id}")
	public ResponseEntity<Channel> findChannelById(@PathVariable("id") UUID userId) {

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
