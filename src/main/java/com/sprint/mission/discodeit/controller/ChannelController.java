package com.sprint.mission.discodeit.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel")
public class ChannelController {

	private final ChannelService channelService;

	// [ ] 공개 채널을 생성할 수 있다.
	@RequestMapping(path = "/public", method = RequestMethod.POST)
	public ResponseEntity<Channel> publicChannel(@RequestBody PublicChannelCreateRequest request) {
		Channel response = channelService.createPublicChannel(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// [ ] 비공개 채널을 생성할 수 있다.
	@RequestMapping(path = "/private", method = RequestMethod.POST)
	public ResponseEntity<Channel> privateChannel(@RequestBody PrivateChannelCreateRequest request) {

		// 개인적으로 비공개 채널은 두명 이상? 은 반드시 필요해야 할 것 같아 두명 이상이어야만 하는 예외처리 생성함.
		if (request.participantsUserIds().size() < 2) {
			throw new IllegalArgumentException("비공개 채널은 두 명이상 부터 생성 가능합니다.");
		}
		Channel response = channelService.createPrivateChannel(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// [ ] 공개 채널의 정보를 수정할 수 있다.
	@RequestMapping(path = "/public/modify", method = RequestMethod.PATCH)
	public ResponseEntity<String> modifyPublicChannel(@RequestBody ChannelUpdateRequest request) {
		channelService.updateChannel(request);

		return new ResponseEntity<>("채널이 수정 완료 되었습니다. : " + request.channelId(), HttpStatus.OK);
	}

	// [ ] 채널을 삭제할 수 있다.
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteChannel(@PathVariable("id") UUID channelId) {
		channelService.deleteChannel(channelId);
		return new ResponseEntity<>("채널이 삭제 완료되었습니다. : " + channelId, HttpStatus.OK);
	}

	// [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
	@RequestMapping(path = "/user/{id}", method =  RequestMethod.GET)
	public ResponseEntity<List<ChannelFindResponse>> findChannelById(@PathVariable("id") UUID userId) {
		List<ChannelFindResponse> responses = channelService.findAllByUserId(userId);

		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}
}
