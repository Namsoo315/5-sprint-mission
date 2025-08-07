package com.sprint.mission.discodeit.mainview;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class TestDataInput {
	public void testData(UserService userService, ChannelService channelService, MessageService messageService) {
		// 유저 생성
		User user1 = userService.createUser(
			new UserCreateRequest("test1", "test1@test.com", "1234", null, null, null, null));
		User user2 = userService.createUser(
			new UserCreateRequest("test2", "test2@test.com", "1234", null, null, null, null));
		User user3 = userService.createUser(
			new UserCreateRequest("test3", "test3@test.com", "1234", null, null, null, null));

		// 채널 생성
		Channel publicChannel1 = channelService.createPublicChannel(
			new PublicChannelCreateRequest("공용 방1", "공용방 입니다."));
		Channel publicChannel2 = channelService.createPublicChannel(
			new PublicChannelCreateRequest("공용 방2", "공용방 입니다."));

		// Private를 위한 List화
		List<UUID> users1 = new ArrayList<>();
		users1.add(user1.getUserId());
		users1.add(user2.getUserId());
		users1.add(user3.getUserId());

		List<UUID> users2 = new ArrayList<>();
		users2.add(user2.getUserId());
		users2.add(user3.getUserId());

		List<UUID> users3 = new ArrayList<>();
		users3.add(user1.getUserId());
		users3.add(user3.getUserId());

		Channel privateChannel1 = channelService.createPrivateChannel(new PrivateChannelCreateRequest(users1));
		Channel privateChannel2 = channelService.createPrivateChannel(new PrivateChannelCreateRequest(users2));
		Channel privateChannel3 = channelService.createPrivateChannel(new PrivateChannelCreateRequest(users3));

		// 메시지 생성

		messageService.createMessage(
			new MessageCreateRequest(user1.getUserId(), publicChannel1.getChannelId(), "안녕하세요 유저1 -> 공용채널1", null));
		messageService.createMessage(
			new MessageCreateRequest(user2.getUserId(), publicChannel2.getChannelId(), "안녕하세요 유저2 -> 공용채널2", null));
		messageService.createMessage(
			new MessageCreateRequest(user1.getUserId(), privateChannel1.getChannelId(), "안녕하세요 유저1 -> 개인채널1", null));
		messageService.createMessage(
			new MessageCreateRequest(user2.getUserId(), privateChannel1.getChannelId(), "안녕하세요 유저2 -> 개인채널1", null));
		messageService.createMessage(
			new MessageCreateRequest(user3.getUserId(), privateChannel1.getChannelId(), "안녕하세요 유저3 -> 개인채널1", null));
		messageService.createMessage(
			new MessageCreateRequest(user3.getUserId(), privateChannel2.getChannelId(), "안녕하세요 유저2 -> 개인채널2", null));
		messageService.createMessage(
			new MessageCreateRequest(user3.getUserId(), privateChannel3.getChannelId(), "안녕하세요 유저3 -> 개인채널3", null));
	}
}
