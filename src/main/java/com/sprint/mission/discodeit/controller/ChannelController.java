package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "채널 관련 API")
public class ChannelController {

  private final ChannelService channelService;

  // [ ] 공개 채널을 생성할 수 있다.
  @PostMapping("/public")
  public ResponseEntity<ApiResponse<Channel>> publicChannel(
      @RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel channel = channelService.createPublicChannel(publicChannelCreateRequest);

    return ResponseEntity.ok(ApiResponse.ok(channel, "공개 채널 생성 완료"));
  }

  // [ ] 비공개 채널을 생성할 수 있다.
  @PostMapping("/private")
  public ResponseEntity<ApiResponse<Channel>> privateChannel(
      @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {

    if (privateChannelCreateRequest.participantsUserIds().size() < 2) {
      throw new IllegalArgumentException("비공개 채널은 두 명이상 부터 생성 가능합니다.");
    }
    Channel channel = channelService.createPrivateChannel(privateChannelCreateRequest);

    return ResponseEntity.ok(ApiResponse.ok(channel, "비공개 채널 생성 완료"));
  }

  // [ ] 공개 채널 정보를 전체 수정할 수 있다.
  @PatchMapping("/{channelId}")
  public ResponseEntity<ApiResponse<String>> modifyPublicChannel(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest channelUpdateRequest) {
    channelService.updateChannel(channelId, channelUpdateRequest);

    return ResponseEntity.ok(ApiResponse.ok(channelId + "의 채널 수정 완료"));
  }

  // [ ] 채널을 삭제할 수 있다.
  @DeleteMapping("/{channelId}")
  public ResponseEntity<ApiResponse<String>> deleteChannel(@PathVariable UUID channelId) {
    channelService.deleteChannel(channelId);
    return ResponseEntity.ok(ApiResponse.ok(channelId + "님의 채널 삭제 완료"));
  }

  // [ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
  @GetMapping("{userId}")
  public ResponseEntity<ApiResponse<List<ChannelFindResponse>>> findChannelById(
      @PathVariable UUID userId) {
    List<ChannelFindResponse> channelFindResponses = channelService.findAllByUserId(userId);

    return ResponseEntity.ok(
        ApiResponse.ok(channelFindResponses, userId + "님의 채널 조회 완료 (공개 채널 포함)"));
  }
}
