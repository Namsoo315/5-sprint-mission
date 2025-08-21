package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus", description = "수신 정보 관련 API")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    // [ ] 특정 채널의 메시지 수신 정보 생성
    @Operation(summary = "메시지 수신 정보 생성 API", responses = {
            @ApiResponse(responseCode = "201", description = "메시지 수신 정보가 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 인자값이 포함되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(
            @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
        ReadStatus readStatus = readStatusService.createReadStatus(readStatusCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus); // 201 Created
    }

    // [ ] 특정 채널의 메시지 수신 정보 수정
    @Operation(summary = "메시지 수신 정보 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "메시지 수신 정보가 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 인자값이 포함되었습니다."),
            @ApiResponse(responseCode = "404", description = "수신 정보 ID가 일치하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> modifyReadStatus(
            @PathVariable UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = readStatusService.updateReadStatus(readStatusId, readStatusUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(readStatus); // 200 OK
    }

    // [ ] 특정 사용자의 메시지 수신 정보 조회
    @Operation(summary = "사용자별 메시지 수신 정보 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "메시지 수신 정보가 정상적으로 조회되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 사용자 ID가 포함되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<ReadStatus>> findReadStatusByUserId(@RequestParam UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(readStatuses); // 200 OK
    }
}
