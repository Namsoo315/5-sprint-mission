package com.sprint.mission.discodeit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
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
	public ResponseEntity<Message> sendMessage(
		@RequestPart MessageCreateRequest messageCreateRequest,
		@RequestPart(required = false) List<MultipartFile> profiles) throws IOException {

		List<BinaryContentDTO> binaryContents = new ArrayList<>();

		if (!profiles.isEmpty()) {
			for (MultipartFile profile : profiles) {
				binaryContents.add(new BinaryContentDTO(profile.getOriginalFilename(), profile.getContentType(),
					profile.getSize(), profile.getBytes()));
			}
		}
		Message response = messageService.createMessage(messageCreateRequest, binaryContents);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// [ ] 메시지를 수정할 수 있다.
	@RequestMapping(path = "/modify", method = RequestMethod.PATCH)
	public ResponseEntity<String> modifyMessage(@RequestBody MessageUpdateRequest messageUpdateRequest) {
		messageService.updateMessage(messageUpdateRequest);

		return new ResponseEntity<>("메시지가 수정이 되었습니다. : " + messageUpdateRequest.getMessageId(), HttpStatus.OK);
	}

	// [ ] 메시지를 삭제할 수 있다.
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteMessage(@PathVariable("id") UUID messageId) {
		messageService.deleteMessage(messageId);

		return new ResponseEntity<>("메시지가 삭제되었습니다. : " + messageId, HttpStatus.OK);
	}

	// [ ] 특정 채널의 메시지 목록을 조회할 수 있다.
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<Message>> findMessageByChannelId(@PathVariable("id") UUID channelId) {
		List<Message> allByChannelId = messageService.findAllByChannelId(channelId);

		return ResponseEntity.status(HttpStatus.OK).body(allByChannelId);
	}
}
