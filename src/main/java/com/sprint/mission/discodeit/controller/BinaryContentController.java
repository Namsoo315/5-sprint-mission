package com.sprint.mission.discodeit.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContent")
public class BinaryContentController {

	private final BinaryContentService binaryContentService;

	// [ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.
	@RequestMapping(path = "/find", method = RequestMethod.GET)
	public ResponseEntity<BinaryContent> findBinaryContent(@RequestParam("binaryContentId") UUID request) {
		BinaryContent response = binaryContentService.findByBinaryContentId(request)
			.orElse(null);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
