package com.sprint.mission.discodeit.mainview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

public class MainViewBasic {

	public void basicMenu(UserService userService, ChannelService channelService, MessageService messageService) {
		// 셋업
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService, user);
		// 테스트
		messageCreateTest(messageService, user, channel);
	}

	static User setupUser(UserService userService) {
		Path path = Paths.get("FileData/testData/test.jpg");

		String fileName;
		String contentType;
		long size;
		byte[] content;

		// BinaryContent 생성을 위한 파일 역질렬화 진행.
		try {
			fileName = path.getFileName().toString();
			contentType = Files.probeContentType(path);
			size = Files.size(path);
			content = Files.readAllBytes(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return userService.createUser(
			new UserCreateRequest("woody", "woody@test.com", "1234", fileName, contentType, size, content));
	}

	static Channel setupChannel(ChannelService channelService, User user) {
		// Public 생성
		channelService.createPublicChannel(new PublicChannelCreateRequest("woody", "woody"));

		// Private 생성 (ReadStatus도 생성 됨)
		List<UUID> userIds = new ArrayList<>();
		userIds.add(user.getUserId());

		// Private를 Message에 주입하기 위한 return 값 지정.
		return channelService.createPrivateChannel(new PrivateChannelCreateRequest(userIds));
	}

	static void messageCreateTest(MessageService messageService, User author, Channel channel) {
		// User, Channel(Private) 주입 후 Message 생성 attachemnet는 넣지 않음.)
		Message message = messageService.createMessage(
			new MessageCreateRequest(author.getUserId(), channel.getChannelId(), "메시지", null));

		System.out.println("메시지 생성: " + message.getMessageId());
		System.out.println(message);
	}

}