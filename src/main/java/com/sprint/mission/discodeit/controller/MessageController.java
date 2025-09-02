package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "메시지 관련 API")
public class MessageController {

  private final MessageService messageService;

  // [ ] 메시지 전송
  @Operation(summary = "메시지 전송 API", responses = {
      @ApiResponse(responseCode = "201", description = "메시지가 정상적으로 전송되었습니다."),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값입니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Message> sendMessage(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> attachments) {

    Message message = messageService.createMessage(messageCreateRequest, attachments);
    return ResponseEntity.status(HttpStatus.CREATED).body(message); // 201 Created
  }

  // [ ] 메시지 수정
  @Operation(summary = "메시지 수정 API", responses = {
      @ApiResponse(responseCode = "200", description = "메시지가 정상적으로 수정되었습니다."),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 값입니다."),
      @ApiResponse(responseCode = "404", description = "메시지를 찾을 수 없습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PatchMapping("/{messageId}")
  public ResponseEntity<Message> modifyMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {
    Message message = messageService.updateMessage(messageId, messageUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(message); // 200 OK
  }

  // [ ] 메시지 삭제
  @Operation(summary = "메시지 삭제 API", responses = {
      @ApiResponse(responseCode = "204", description = "메시지가 정상적으로 삭제되었습니다."),
      @ApiResponse(responseCode = "404", description = "메시지를 찾을 수 없습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessage(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
  }

  // [ ] 특정 채널의 메시지 조회
  @Operation(summary = "채널별 메시지 조회 API", responses = {
      @ApiResponse(responseCode = "200", description = "메시지가 정상적으로 조회되었습니다."),
      @ApiResponse(responseCode = "404", description = "채널을 찾을 수 없습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping
  public ResponseEntity<List<Message>> findMessageByChannelId(@RequestParam UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.status(HttpStatus.OK).body(messages); // 200 OK
  }
}
