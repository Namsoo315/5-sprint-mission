package com.sprint.mission.discodeit.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Tag(name = "message", description = "메시지 관련 API")
public class MessageController {

  private final MessageService messageService;

  // [ ] 메시지 전송
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Message> sendMessage(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> attachments) throws IOException {

    List<BinaryContentDTO> binaryContents = new ArrayList<>();

    if (attachments != null && !attachments.isEmpty()) {
      for (MultipartFile file : attachments) {
        binaryContents.add(new BinaryContentDTO(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize(),
            file.getBytes()
        ));
      }
    }

    Message message = messageService.createMessage(messageCreateRequest, binaryContents);
    return ResponseEntity.status(HttpStatus.CREATED).body(message); // 201 Created
  }

  // [ ] 메시지 수정
  @PatchMapping("/{messageId}")
  public ResponseEntity<Message> modifyMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {
    Message message = messageService.updateMessage(messageId, messageUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(message); // 200 OK
  }

  // [ ] 메시지 삭제
  @DeleteMapping("/{messageId}")
  public ResponseEntity<String> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessage(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(messageId + "님의 메시지 삭제 완료"); // 204 No Content
  }

  // [ ] 특정 채널의 메시지 조회
  @GetMapping
  public ResponseEntity<List<Message>> findMessageByChannelId(@RequestParam UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.status(HttpStatus.OK).body(messages); // 200 OK
  }
}
