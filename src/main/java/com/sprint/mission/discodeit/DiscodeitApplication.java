package com.sprint.mission.discodeit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mainview.MainViewBasic;
import com.sprint.mission.discodeit.mainview.MainViewTest;
import com.sprint.mission.discodeit.mainview.TestDataInput;
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
		UserStatusService userStatusService = context.getBean("userStatusService", UserStatusService.class);
		ReadStatusService readStatusService = context.getBean("readStatusService", ReadStatusService.class);
		BinaryContentService binaryContentService = context.getBean("binaryContentService", BinaryContentService.class);

		MainViewTest mainViewTest = new MainViewTest();
		mainViewTest.mainMenu(userService, channelService, messageService, userStatusService, readStatusService, binaryContentService);
		// MainViewBasic mainViewBasic = new MainViewBasic();
		//
		// mainViewBasic.basicMenu(userService, channelService, messageService);
	}
}
