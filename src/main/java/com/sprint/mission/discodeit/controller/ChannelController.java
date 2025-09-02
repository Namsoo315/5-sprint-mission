package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "채널 관련 API")
public class ChannelController {

  private final ChannelService channelService;

  // [ ] 공개 채널 생성
  @Operation(summary = "공개 채널 생성 API", responses = {
      @ApiResponse(responseCode = "201", description = "공개 채널이 정상적으로 생성되었습니다."),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping("/public")
  public ResponseEntity<ChannelDTO> publicChannel(
      @RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
    ChannelDTO channel = channelService.createPublicChannel(publicChannelCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

  // [ ] 비공개 채널 생성
  @Operation(summary = "비공개 채널 생성 API", responses = {
      @ApiResponse(responseCode = "201", description = "비공개 채널이 정상적으로 생성되었습니다."),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping("/private")
  public ResponseEntity<ChannelDTO> privateChannel(
      @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {

    if (privateChannelCreateRequest.participantIds().size() < 2) {
      throw new IllegalArgumentException("비공개 채널은 두 명 이상부터 생성 가능합니다.");
    }
    ChannelDTO channel = channelService.createPrivateChannel(privateChannelCreateRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

  // [ ] 공개 채널 정보 수정
  @Operation(summary = "공개 채널 수정 API", responses = {
      @ApiResponse(responseCode = "200", description = "공개 채널이 수정 완료되었습니다."),
      @ApiResponse(responseCode = "400", description = "잘못된 인자값이 포함되어 있습니다."),
      @ApiResponse(responseCode = "404", description = "채널 아이디를 찾을 수 없습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDTO> modifyPublicChannel(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest channelUpdateRequest) {
    ChannelDTO dto = channelService.updateChannel(channelId, channelUpdateRequest);
    return ResponseEntity.ok(dto);
  }

  // [ ] 채널 삭제
  @Operation(summary = "채널 삭제 API", responses = {
      @ApiResponse(responseCode = "204", description = "채널이 정상적으로 삭제되었습니다."),
      @ApiResponse(responseCode = "404", description = "채널 아이디를 찾을 수 없습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.deleteChannel(channelId);
    return ResponseEntity.noContent().build(); // 204 No Content
  }

  // [ ] 특정 사용자가 볼 수 있는 모든 채널 조회
  @Operation(summary = "특정 사용자의 채널 조회 API", responses = {
      @ApiResponse(responseCode = "200", description = "채널 목록을 성공적으로 조회했습니다."),
      @ApiResponse(responseCode = "404", description = "해당 사용자의 채널을 찾을 수 없습니다."),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping
  public ResponseEntity<List<ChannelDTO>> findChannelById(@RequestParam UUID userId) {
    List<ChannelDTO> channelFindResponses = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channelFindResponses);
  }
}
