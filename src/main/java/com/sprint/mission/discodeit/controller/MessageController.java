package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
@Tag(name = "message", description = "메시지 관련 API")
public class MessageController {

  private final MessageService messageService;

  // [ ] 메시지를 보낼 수 있다.
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<Message>> sendMessage(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {

    List<BinaryContentDTO> binaryContents = new ArrayList<>();

    if (multipartFiles != null && !multipartFiles.isEmpty()) {
      for (MultipartFile file : multipartFiles) {
        binaryContents.add(new BinaryContentDTO(file.getOriginalFilename(), file.getContentType(),
            file.getSize(), file.getBytes()));
      }
    }
    Message message = messageService.createMessage(messageCreateRequest, binaryContents);

    return ResponseEntity.ok(ApiResponse.ok(message, "메시지 생성 완료"));
  }

  // [ ] 메시지를 수정할 수 있다.	(Content 하나이기에 Put으로 지정함)
  @PutMapping("/{messageId}")
  public ResponseEntity<ApiResponse<String>> modifyMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {
    messageService.updateMessage(messageId, messageUpdateRequest);

    return ResponseEntity.ok(ApiResponse.ok(messageId + "님의 메시지 수정 완료"));
  }

  // [ ] 메시지를 삭제할 수 있다.
  @DeleteMapping("/{messageId}")
  public ResponseEntity<ApiResponse<String>> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessage(messageId);

    return ResponseEntity.ok(ApiResponse.ok(messageId + "님의 메시지 삭제 완료"));
  }

  // [ ] 특정 채널의 메시지 목록을 조회할 수 있다.
  @GetMapping("/channels/{channelId}/messages")
  public ResponseEntity<ApiResponse<List<Message>>> findMessageByChannelId(
      @PathVariable UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);

    return ResponseEntity.ok(ApiResponse.ok(messages, channelId + "님의 채널 조회 완료"));
  }
}
