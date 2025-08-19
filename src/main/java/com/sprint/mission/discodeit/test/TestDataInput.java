package com.sprint.mission.discodeit.test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataInput {

  private final UserService userService;
  private final ChannelService channelService;
  private final MessageService messageService;

  @PostConstruct
  public void init() {
    testData();
  }

  @PreDestroy
  public void cleanup() {
    for (UserDto user : userService.findAll()) {
      userService.deleteUser(user.userId());
      for (ChannelFindResponse channel : channelService.findAllByUserId(user.userId())) {
        channelService.deleteChannel(channel.channelId());
        for (Message message : messageService.findAllByChannelId(channel.channelId())) {
          messageService.deleteMessage(message.getMessageId());
        }
      }
    }
  }

  public void testData() {
    // 유저 생성
    BinaryContentDTO binaryContentDTO = null;

    User user1 = userService.createUser(new UserCreateRequest("test1", "test1@test.com", "1234"),
        binaryContentDTO);
    User user2 = userService.createUser(new UserCreateRequest("test2", "test2@test.com", "1234"),
        binaryContentDTO);
    User user3 = userService.createUser(new UserCreateRequest("test3", "test3@test.com", "1234"),
        binaryContentDTO);

    // 채널 생성
    Channel publicChannel1 = channelService.createPublicChannel(
        new PublicChannelCreateRequest("공용 방1", "공용방 입니다."));
    Channel publicChannel2 = channelService.createPublicChannel(
        new PublicChannelCreateRequest("공용 방2", "공용방 입니다."));

    // Private 채널 유저 리스트 준비
    List<UUID> users1 = List.of(user1.getUserId(), user2.getUserId(), user3.getUserId());
    List<UUID> users2 = List.of(user2.getUserId(), user3.getUserId());
    List<UUID> users3 = List.of(user1.getUserId(), user3.getUserId());

    // Private 채널 생성
    Channel privateChannel1 = channelService.createPrivateChannel(
        new PrivateChannelCreateRequest(users1));
    Channel privateChannel2 = channelService.createPrivateChannel(
        new PrivateChannelCreateRequest(users2));
    Channel privateChannel3 = channelService.createPrivateChannel(
        new PrivateChannelCreateRequest(users3));

    List<BinaryContentDTO> dtos = new ArrayList<>();

    // 메시지 생성
    messageService.createMessage(
        new MessageCreateRequest(user1.getUserId(), publicChannel1.getChannelId(),
            "안녕하세요 유저1 -> 공용채널1"), dtos);
    messageService.createMessage(
        new MessageCreateRequest(user2.getUserId(), publicChannel2.getChannelId(),
            "안녕하세요 유저2 -> 공용채널2"), dtos);
    messageService.createMessage(
        new MessageCreateRequest(user1.getUserId(), privateChannel1.getChannelId(),
            "안녕하세요 유저1 -> 개인채널1"), dtos);
    messageService.createMessage(
        new MessageCreateRequest(user2.getUserId(), privateChannel1.getChannelId(),
            "안녕하세요 유저2 -> 개인채널1"), dtos);
    messageService.createMessage(
        new MessageCreateRequest(user3.getUserId(), privateChannel1.getChannelId(),
            "안녕하세요 유저3 -> 개인채널1"), dtos);
    messageService.createMessage(
        new MessageCreateRequest(user3.getUserId(), privateChannel2.getChannelId(),
            "안녕하세요 유저2 -> 개인채널2"), dtos);
    messageService.createMessage(
        new MessageCreateRequest(user3.getUserId(), privateChannel3.getChannelId(),
            "안녕하세요 유저3 -> 개인채널3"), dtos);
  }
}
