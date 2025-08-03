package com.sprint.mission.discodeit.mainview;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class MainViewBasic {

	public void mainMenuBasic(){
		// 서비스 초기화
		// TODO Basic*Service 구현체를 초기화하세요.
		UserService userService = new BasicUserService(new JCFUserRepository());
		ChannelService channelService = new BasicChannelService(new JCFChannelRepository());
		MessageService messageService = new BasicMessageService(new JCFMessageRepository());

		// 셋업
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);
		// 테스트
		messageCreateTest(messageService, channel, user);
	}
	static User setupUser(UserService userService) {
		//"woody", "woody@codeit.com", "woody1234" 원래 이렇게 넣어야 하지만 name이랑 age만 받아서 이런식을 처리함.
		User user = userService.createUser("woody", 15);
		return user;
	}

	static Channel setupChannel(ChannelService channelService) {
		// ChannelType.PUBLIC Enum 타입으로 정하지 않아서 그냥 뺐음.
		Channel channel = channelService.createChannel( "공지", "공지 채널입니다.");
		return channel;
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		//"안녕하세요.", channel.getId(), author.getId()	createMessage로 userId, channelId, content를 받기 때문에 변경.
		Message message = messageService.createMessage( author.getUserId(), channel.getChannelId(), "안녕하세요");
		System.out.println("메시지 생성: " + message.getMessageId());
		System.out.println(message);
	}
}
