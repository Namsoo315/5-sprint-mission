package com.sprint.mission.discodeit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sprint.mission.discodeit.entity.BinaryContent;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/binaryContent")
public class BinaryContentController {

	@RequestMapping("/download")
	public ResponseEntity<BinaryContent> download() {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
