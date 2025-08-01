package com.sprint.mission.discodeit.mainview;

import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class TestDataInput {
	public void testData(UserService userService, ChannelService channelService, MessageService messageService) {
		User user1 = userService.createUser(new UserCreateRequest("남현수1", "namsoo1@test1.com", "1234"));
		User user2 = userService.createUser(new UserCreateRequest("남현수2", "namsoo2@test2.com", "1234"));
		User user3 = userService.createUser(new UserCreateRequest("남현수3", "namsoo3@test3.com", "1234"));

		Channel channel1 = channelService.createPublicChannel(new PublicChannelCreateRequest("채널 1", "스터디 공부방입니다."));
		Channel channel2 = channelService.createPublicChannel(new PublicChannelCreateRequest("채널 2", "커뮤니티 노는방입니다."));

		// messageService.createMessage(user1.getUserId(), channel2.getChannelId(), "user1의 두번째 메시지");
		// messageService.createMessage(user1.getUserId(), channel2.getChannelId(), "user1의 세번째 메시지");
		// messageService.createMessage(user2.getUserId(), channel1.getChannelId(), "user2의 첫번째 메시지");
		// messageService.createMessage(user2.getUserId(), channel2.getChannelId(), "user2의 두번째 메시지");
		// messageService.createMessage(user3.getUserId(), channel1.getChannelId(), "user3의 첫번째 메시지");
	}

}
