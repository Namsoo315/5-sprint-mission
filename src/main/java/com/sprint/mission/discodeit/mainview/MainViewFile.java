package com.sprint.mission.discodeit.mainview;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class MainViewFile {
	public void mainMenu() {
		Scanner sc = new Scanner(System.in);

		UserService fileUserService = new FileUserService();
		ChannelService fileChannelService = new FileChannelService();
		MessageService fileMessageService = new FileMessageService();

		// Test Data Input
		TestDataInput testDataInput = new TestDataInput();
		testDataInput.testData(fileUserService, fileChannelService, fileMessageService);
		System.out.println();
		System.out.println("===== 1. userCRUDTest =====");
		System.out.println("===== 2. channelCRUDTest =====");
		System.out.println("===== 3. messageCRUDTest =====");


		System.out.print("보고싶은 CRUDTest : ");
		int i = sc.nextInt();

		try {
			switch (i) {
				case 1:
					userCRUDTest(fileUserService);
					break;
				case 2:
					channelCRUDTest(fileChannelService);
					break;
				case 3:
					messageCRUDTest(fileUserService, fileChannelService, fileMessageService);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("유효하지 않는 값입니다.");
		} finally {
			//파일 계속 저장되고 삭제되는게 귀찮아서 잠시 만듬.
			allDeleteFile(fileUserService, fileChannelService, fileMessageService);
		}
	}

	private static void allDeleteFile(UserService userService, ChannelService channelService,
		MessageService messageService) {
		//전체 삭제
		for (User user1 : userService.findAll()) {
			userService.deleteUser(user1.getUserId());
		}

		//전체 삭제
		for (Channel channel1 : channelService.findAll()) {
			channelService.deleteChannel(channel1.getChannelId());
		}

		//전체 삭제
		for(Message m : messageService.findAll()) {
			messageService.deleteMessage(m.getMessageId());
		}
	}

	private static void userCRUDTest(UserService userService) {
		//등록
		System.out.println("===== User 생성 =====");
		User user = userService.createUser("남현수", 30);
		System.out.println("User 생성 : " + user.getUserId() + " 이름 : " + user.getUsername());
		System.out.println();

		//조회(단건)
		System.out.println("===== User 하나만 조회 =====");
		Optional<User> byId = userService.findByUserId(user.getUserId());
		byId.ifPresent(u -> System.out.println("유저 하나 조회 : " + u.getUserId()));
		System.out.println();

		//조회(다건)
		System.out.println("===== User 전부 조회 =====");
		userService.findAll().forEach(u -> System.out.println(u.toString()));
		System.out.println();

		//수정
		System.out.println("===== User 수정 =====");
		System.out.println("수정되는 UUID : " + user.getUserId());
		System.out.println();
		String updateName = "수정된 이름";
		int updateAge = 100;
		userService.updateUser(user.getUserId(), updateName, updateAge);        //수정 시작

		//수정된 데이터 조회
		System.out.println("===== User 수정 조회=====");
		System.out.println(user);
		System.out.println();

		//수정 후 전체 데이터 조회도
		System.out.println("===== User 수정후 전체 조회=====");
		List<User> modifyUsers = userService.findAll();
		modifyUsers.forEach(u -> System.out.println(u.toString()));
		System.out.println();

		//삭제
		System.out.println("===== User 삭제=====");
		System.out.println("삭제할 UUID : " + user.getUserId());
		userService.deleteUser(user.getUserId());
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== User 삭제 조회=====");
		List<User> deleteUsers = userService.findAll();
		deleteUsers.forEach(u -> System.out.println(u.toString()));
		System.out.println();


	}

	private static void channelCRUDTest(ChannelService channelService) {
		//등록
		System.out.println("===== Channel 생성 =====");
		Channel channel = channelService.createChannel("새로운 방", "새로운 방입니다.");
		System.out.println(channel.toString());
		System.out.println();

		//조회(단건)
		System.out.println("===== Channel 조회 (단건) =====");        //정확히 단건은 아니지만 List사용을 해보고싶었음.
		List<Channel> byChannelName = channelService.findByChannelName(channel.getName());
		System.out.println(byChannelName.toString());
		System.out.println();

		//조회(다건)
		System.out.println("===== Channel 전부 조회 =====");
		List<Channel> byAllChannel = channelService.findAll();
		byAllChannel
			.stream()
			.sorted(Comparator.comparing(Channel::getName)).
			forEach(ch -> System.out.println(ch.toString()));
		System.out.println();

		//수정
		System.out.println("===== Channel 수정 =====");
		System.out.println("수정되는 Channel ID : " + channel.getChannelId());
		String channelNameModify = "수정된 방 이름";
		channelService.updateChannel(channel.getChannelId(), channelNameModify, "수정된 내용");
		System.out.println();

		//수정된 데이터 조회
		System.out.println("===== 수정된 데이터 조회 =====");
		List<Channel> modifyChannelFind = channelService.findByChannelName(channelNameModify);
		System.out.println("수정된 데이터 조회 : " + modifyChannelFind);
		System.out.println();

		//수정 후 전체 데이터 조회도
		System.out.println("===== Channel 전체 데이터 조회 =====");
		channelService.findAll().forEach(ch -> System.out.println(ch.toString()));
		System.out.println();

		//삭제
		System.out.println("===== Channel 삭제 =====");
		System.out.println("삭제될 데이터 ID : " + channel.getChannelId());
		channelService.deleteChannel(channel.getChannelId());
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== Channel 삭제 확인 =====");
		channelService.findAll().forEach(ch -> System.out.println(ch.toString()));
		System.out.println();

	}

	private static void messageCRUDTest(UserService userService, ChannelService channelService,
		MessageService messageService) {

		//등록
		System.out.println("===== Message 생성 =====");

		User user1 = userService.createUser("Message Test용 User", 15);
		Channel channel1 = channelService.createChannel("Message Test용 Channel", "메시지 테스트 용입니다.");

		Message message = messageService.createMessage(user1.getUserId(), channel1.getChannelId(), "첫번째 메시지");
		System.out.println(message.toString());
		System.out.println();

		//조회(단건)
		System.out.println("===== Message 하나만 조회 =====");
		Optional<Message> foundMessage = messageService.findByMessageId(message.getMessageId());
		foundMessage.ifPresent(m -> System.out.println("조회된 Message: " + m.toString()));
		System.out.println();

		//조회(다건)
		System.out.println("===== Message 전부 조회 =====");
		List<Message> allMessages = messageService.findAll();
		allMessages.forEach(m -> System.out.println(m.toString()));
		System.out.println();

		//수정
		System.out.println("===== Message 수정 =====");
		String newContent = "수정된 첫번째 메시지";
		messageService.updateMessage(message.getMessageId(), newContent);
		System.out.println();

		//수정된 데이터 조회
		System.out.println("===== Message 수정 조회=====");
		Optional<Message> updatedMessage = messageService.findByMessageId(message.getMessageId());
		updatedMessage.ifPresent(m -> System.out.println("수정된 Message : " + m));
		System.out.println();

		//수정 후 전체 데이터 조회도
		System.out.println("===== Message 수정후 전체 조회=====");
		List<Message> messages = messageService.findAll();
		messages.forEach(m -> System.out.println(m.toString()));

		//삭제
		System.out.println("===== Message 삭제=====");
		messageService.deleteMessage(message.getMessageId());
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== Message 삭제 조회=====");
		List<Message> afterDeleteMessages = messageService.findAll();
		afterDeleteMessages.forEach(m -> System.out.println(m.toString()));
		System.out.println();

	}
}
