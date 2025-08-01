package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

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
		messageCreateTest(messageService, channel, user);
	}

	static User setupUser(UserService userService) {
		//"woody", "woody@codeit.com", "woody1234" 원래 이렇게 넣어야 하지만 name이랑 age만 받아서 이런식을 처리함.
		return userService.createUser(new UserCreateRequest("woody", "woody@test.com","1234"));
	}

	static Channel setupChannel(ChannelService channelService) {
		// ChannelType.PUBLIC Enum 타입으로 정하지 않아서 그냥 뺐음.
		return channelService.createPublicChannel(new PublicChannelCreateRequest("woody", "woody"));
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		//"안녕하세요.", channel.getId(), author.getId()	createMessage로 userId, channelId, content를 받기 때문에 변경.
		Message message = messageService.createMessage(author.getUserId(), channel.getChannelId(), "안녕하세요");
		System.out.println("메시지 생성: " + message.getMessageId());
		System.out.println(message);
	}

}
