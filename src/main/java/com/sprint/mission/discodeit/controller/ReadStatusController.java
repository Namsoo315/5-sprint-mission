package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus", description = "수신 정보 관련 API")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  // [ ] 특정 채널의 메시지 수신 정보 생성
  @PostMapping
  public ResponseEntity<ReadStatus> createReadStatus(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
    ReadStatus readStatus = readStatusService.createReadStatus(readStatusCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus); // 201 Created
  }

  // [ ] 특정 채널의 메시지 수신 정보 수정
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<String> modifyReadStatus(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusService.updateReadStatus(readStatusId,
        readStatusUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatus.getChannelId() + " 채널의 메시지 수신정보 수정 완료"); // 200 OK
  }

  // [ ] 특정 사용자의 메시지 수신 정보 조회
  @GetMapping("{userId}/users")
  public ResponseEntity<List<ReadStatus>> findReadStatusByUserId(@PathVariable UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(readStatuses); // 200 OK
  }
}
