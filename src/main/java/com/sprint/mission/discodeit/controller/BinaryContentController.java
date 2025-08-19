package com.sprint.mission.discodeit.controller;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
	@RequestMapping(path = "/find/{binaryContentId}", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse<BinaryContent>> findBinaryContent(@PathVariable UUID binaryContentId) {
		BinaryContent binaryContent = binaryContentService.findByBinaryContentId(binaryContentId);

		return ResponseEntity.ok(ApiResponse.ok(binaryContent, "파일 조회 완료 [단회]" ));
	}

	@RequestMapping(path ="/find", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse<List<BinaryContent>>> findAllBinaryContent(@RequestParam List<UUID> binaryContentIds) {
		List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);

		return ResponseEntity.ok(ApiResponse.ok(binaryContents, "파일 조회 완료 [다회]"));
	}
}
