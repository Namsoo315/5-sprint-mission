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
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class MainView {
	public void mainMenu() {
		Scanner sc = new Scanner(System.in);
		UserService userService = new JCFUserService();
		ChannelService channelService = new JCFChannelService();
		MessageService messageService = new JCFMessageService(userService, channelService);

		System.out.println("보고싶은 CRUDTest");
		System.out.println("===== 1. userCRUDTest =====");
		System.out.println("===== 2. channelCRUDTest =====");
		System.out.println("===== 3. messageCRUDTest =====");

		int i = sc.nextInt();

		try {
			switch (i) {
				case 1:
					userCRUDTest(userService);
					break;
				case 2:
					channelCRUDTest(channelService);
					break;
				case 3:
					messageCRUDTest(userService, channelService, messageService);
					break;
			}
		} catch (Exception e) {
			throw new IllegalStateException("유효하지 않는 값입니다.");
		}

	}

	private static void userCRUDTest(UserService userService) {
		//등록
		System.out.println("===== User 생성 =====");
		User user = userService.createUser("남현수", 30);
		System.out.println("User 생성 : " + user.getId() + " 이름 : " + user.getUsername());
		System.out.println();

		//조회(단건)
		System.out.println("===== User 하나만 조회 =====");
		Optional<User> byId = userService.findById(user.getId());
		byId.ifPresent(u -> System.out.println("유저 하나 조회 : " + u.getId()));
		System.out.println();

		//조회(다건)
		System.out.println("===== User 전부 조회 =====");
		userService.findByAll().forEach(u -> System.out.println(u.toString()));
		System.out.println();

		//수정
		System.out.println("===== User 수정 =====");
		System.out.println("수정되는 UUID : " + user.getId());
		System.out.println();
		String updateName = "수정된 이름";
		int updateAge = 100;
		userService.updateUser(user.getId(), updateName, updateAge);        //수정 시작

		//수정된 데이터 조회
		System.out.println("===== User 수정 조회=====");
		System.out.println(user);
		System.out.println();

		//수정 후 전체 데이터 조회도
		System.out.println("===== User 수정후 전체 조회=====");
		List<User> modifyUsers = userService.findByAll();
		modifyUsers.forEach(u -> System.out.println(u.toString()));
		System.out.println();

		//삭제
		System.out.println("===== User 삭제=====");
		System.out.println("삭제할 UUID : " + user.getId());
		userService.deleteUser(user.getId());
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== User 삭제 조회=====");
		List<User> deleteUsers = userService.findByAll();
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
		List<Channel> byAllChannel = channelService.findByAllChannel();
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
		channelService.findByAllChannel().forEach(ch -> System.out.println(ch.toString()));
		System.out.println();

		//삭제
		System.out.println("===== Channel 삭제 =====");
		System.out.println("삭제될 데이터 ID : " + channel.getChannelId());
		channelService.deleteChannel(channel.getChannelId());
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== Channel 삭제 확인 =====");
		channelService.findByAllChannel().forEach(ch -> System.out.println(ch.toString()));
		System.out.println();
	}

	private static void messageCRUDTest(UserService userService, ChannelService channelService,
		MessageService messageService) {

		//등록
		System.out.println("===== Message 생성 =====");

		// 원래는 findById해서 전부 찾아와야하지만 할게 많아 보여서 이렇게 함.
		User user1 = userService.findByAll().stream().findFirst()
			.orElseThrow(() -> new IllegalStateException("등록된 유저가 없습니다."));
		User user2 = userService.createUser("나무나무나무", 15);
		Channel channel = channelService.findByAllChannel().stream().findFirst()
			.orElseThrow(() -> new IllegalStateException("등록된 채널이 없습니다."));

		Message message = messageService.createMessage(user1.getId(), channel.getChannelId(), "첫번째 메시지");
		messageService.createMessage(user1.getId(), channel.getChannelId(), "두번째 메시지");
		messageService.createMessage(user1.getId(), channel.getChannelId(), "세번째 메시지");

		messageService.createMessage(user2.getId(), channel.getChannelId(), "user2의 첫번째 메시지");
		messageService.createMessage(user2.getId(), channel.getChannelId(), "user2의 두번째 메시지");

		System.out.println(message.toString());
		System.out.println();

		//조회(단건)
		System.out.println("===== Message 하나만 조회 =====");
		Optional<Message> foundMessage = messageService.findByMessage(
			message.getMessageId(), user1.getId(), channel.getChannelId()
		);
		foundMessage.ifPresent(m -> System.out.println("조회된 Message: " + m.toString()));
		System.out.println();

		//조회(다건)
		System.out.println("===== Message 전부 조회 =====");
		List<Message> allMessages = messageService.findByAllMessage();
		allMessages.forEach(m -> System.out.println(m.toString()));
		System.out.println();

		//수정
		System.out.println("===== Message 수정 =====");
		String newContent = "수정된 첫번째 메시지";
		messageService.updateMessage(message.getMessageId(), user1.getId(), channel.getChannelId(), newContent);

		//수정된 데이터 조회
		System.out.println("===== Message 수정 조회=====");
		Optional<Message> updatedMessage = messageService.findByMessage(
			message.getMessageId(), user1.getId(), channel.getChannelId()
		);
		updatedMessage.ifPresent(m -> System.out.println("수정된 Message : " + m.toString()));
		System.out.println();

		//수정 후 전체 데이터 조회도
		System.out.println("===== Message 수정후 전체 조회=====");
		List<Message> messages = messageService.findByAllMessage();
		messages.forEach(m -> System.out.println(m.toString()));

		//삭제
		System.out.println("===== Message 삭제=====");
		messageService.deleteMessage(message.getMessageId(), user1.getId(), channel.getChannelId());
		System.out.println("삭제 완료: '" + newContent + "'");
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== Message 삭제 조회=====");
		List<Message> afterDeleteMessages = messageService.findByAllMessage();
		afterDeleteMessages.forEach(m -> System.out.println(m.toString()));
		System.out.println();

	}
}
