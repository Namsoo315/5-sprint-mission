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

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/readStatus")
public class ReadStatusController {
	private final ReadStatusService readStatusService;

	// [ ] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
	@RequestMapping(path = "/", method = RequestMethod.POST)
	public ResponseEntity<ReadStatus> CreateReadStatus(@RequestBody ReadStatusCreateRequest request) {
		ReadStatus response = readStatusService.createReadStatus(request);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// [ ] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
	@RequestMapping(path = "/modify", method = RequestMethod.PATCH)
	public ResponseEntity<ReadStatus> ModifyReadStatus(@RequestBody ReadStatusUpdateRequest request) {
		ReadStatus response = readStatusService.updateReadStatus(request);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// [ ] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)	//특정 사용자가 누구인가?
	public ResponseEntity<List<ReadStatus>> findReadStatusById(@PathVariable("id") UUID userId) {
		List<ReadStatus> responses = readStatusService.findAllByUserId(userId);

		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}
}
