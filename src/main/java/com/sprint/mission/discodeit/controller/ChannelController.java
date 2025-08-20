package com.sprint.mission.discodeit.controller;

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
import org.springframework.http.HttpStatus;
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

  // [ ] 공개 채널 생성
  @PostMapping("/public")
  public ResponseEntity<Channel> publicChannel(
      @RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel channel = channelService.createPublicChannel(publicChannelCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(channel); // 201 Created
  }

  // [ ] 비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<Channel> privateChannel(
      @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {

    if (privateChannelCreateRequest.participantsUserIds().size() < 2) {
      throw new IllegalArgumentException("비공개 채널은 두 명 이상부터 생성 가능합니다.");
    }
    Channel channel = channelService.createPrivateChannel(privateChannelCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(channel); // 201 Created
  }

  // [ ] 공개 채널 정보 수정
  @PatchMapping("/{channelId}")
  public ResponseEntity<String> modifyPublicChannel(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest channelUpdateRequest) {
    channelService.updateChannel(channelId, channelUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(channelId + "의 채널 수정 완료"); // 200 OK
  }

  // [ ] 채널 삭제
  @DeleteMapping("/{channelId}")
  public ResponseEntity<String> deleteChannel(@PathVariable UUID channelId) {
    channelService.deleteChannel(channelId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(channelId + "님의 채널 삭제 완료"); // 204 No Content
  }

  // [ ] 특정 사용자가 볼 수 있는 모든 채널 조회
  @GetMapping("{userId}")
  public ResponseEntity<List<ChannelFindResponse>> findChannelById(@PathVariable UUID userId) {
    List<ChannelFindResponse> channelFindResponses = channelService.findAllByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(channelFindResponses); // 200 OK
  }
}
