package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean("userService", UserService.class);
		ChannelService channelService = context.getBean("channelService", ChannelService.class);
		MessageService messageService = context.getBean("messageService", MessageService.class);

		// 셋업
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);

		// 테스트
		messageCreateTest(messageService, user, channel);
	}

	static User setupUser(UserService userService) {
		return userService.createUser(new UserCreateRequest("woody", "woody@test.com", "1234", null, null, null, null));
	}

	static Channel setupChannel(ChannelService channelService) {
		// ChannelType.PUBLIC Enum 타입으로 정하지 않아서 그냥 뺐음.
		return channelService.createPublicChannel(new PublicChannelCreateRequest("woody", "woody"));
	}

	static void messageCreateTest(MessageService messageService, User author, Channel channel) {
		Message message = messageService.createMessage(
			new MessageCreateRequest(author.getUserId(), channel.getChannelId(), "메시지", null));
		System.out.println("메시지 생성: " + message.getMessageId());
		System.out.println(message);
	}

}
