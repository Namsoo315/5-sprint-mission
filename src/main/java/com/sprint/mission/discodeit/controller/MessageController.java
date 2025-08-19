package com.sprint.mission.discodeit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.ApiResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

	private final MessageService messageService;

	// [ ] 메시지를 보낼 수 있다.
	@RequestMapping(path = "/",
		method = RequestMethod.POST,
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Message>> sendMessage(
		@RequestPart MessageCreateRequest messageCreateRequest,
		@RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {

		List<BinaryContentDTO> binaryContents = new ArrayList<>();

		if (!multipartFiles.isEmpty()) {
			for (MultipartFile file : multipartFiles) {
				binaryContents.add(new BinaryContentDTO(file.getOriginalFilename(), file.getContentType(),
					file.getSize(), file.getBytes()));
			}
		}
		Message message = messageService.createMessage(messageCreateRequest, binaryContents);

		return ResponseEntity.ok(ApiResponse.ok(message, "메시지 생성 완료"));
	}

	// [ ] 메시지를 수정할 수 있다.
	@RequestMapping(path = "/modify", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse<String>> modifyMessage(@RequestBody MessageUpdateRequest messageUpdateRequest) {
		messageService.updateMessage(messageUpdateRequest);

		return ResponseEntity.ok(ApiResponse.ok(messageUpdateRequest.messageId() + "님의 메시지 수정 완료"));
	}

	// [ ] 메시지를 삭제할 수 있다.
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ApiResponse<String>> deleteMessage(@PathVariable("id") UUID messageId) {
		messageService.deleteMessage(messageId);

		return ResponseEntity.ok(ApiResponse.ok(messageId + "님의 메시지 삭제 완료"));
	}

	// [ ] 특정 채널의 메시지 목록을 조회할 수 있다.
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ApiResponse<List<Message>>> findMessageByChannelId(@PathVariable("id") UUID channelId) {
		List<Message> messages = messageService.findAllByChannelId(channelId);

		return ResponseEntity.ok(ApiResponse.ok(messages, channelId + "님의 채널 조회 완료"));
	}
}
