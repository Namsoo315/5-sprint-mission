package com.sprint.mission.discodeit.mainview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserFindRequest;
import com.sprint.mission.discodeit.dto.user.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

public class MainViewTest {
	public void mainMenu(UserService userService, ChannelService channelService, MessageService messageService,
		UserStatusService userStatusService, ReadStatusService readStatusService,
		BinaryContentService binaryContentService) {
		Scanner sc = new Scanner(System.in);

		// Test Data Input
		TestDataInput testDataInput = new TestDataInput();
		testDataInput.testData(userService, channelService, messageService);

		System.out.println();
		System.out.println("===== 1. userCRUDTest =====");
		System.out.println("===== 2. channelCRUDTest =====");
		System.out.println("===== 3. messageCRUDTest =====");

		System.out.print("보고싶은 CRUDTest : ");
		int i = sc.nextInt();

		// 셋업
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService, user);

		try {
			switch (i) {
				case 1:
					userCRUDTest(userService);
					break;
				case 2:
					channelCRUDTest(channelService, userService);
					break;
				case 3:
					messageCRUDTest(user, channel, messageService);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("유효하지 않는 값입니다.");
		}
		// finally {
		// 		//파일 계속 저장되고 삭제되는게 귀찮아서 잠시 만듬.
		// 		allDeleteFile(fileUserService, fileChannelService, fileMessageService);
		// 	}
	}

	// private static void allDeleteFile(UserService userService, ChannelService channelService,
	// 	MessageService messageService) {
	// 	//전체 삭제
	// 	for (User user : userService.findAll()) {
	// 		userService.deleteUser(user.getUserId());
	// 	}
	//
	// 	//전체 삭제
	// 	for (Channel channel1 : channelService.findAll()) {
	// 		channelService.deleteChannel(channel1.getChannelId());
	// 	}
	//
	// 	//전체 삭제
	// 	for (Message m : messageService.findAll()) {
	// 		messageService.deleteMessage(m.getMessageId());
	// 	}
	// }

	private static void userCRUDTest(UserService userService) {
		//등록
		System.out.println("===== User 생성 =====");
		User user = userService.createUser(
			new UserCreateRequest("남현수", "namsoo@user.com", "1234", null, null, null, null));
		System.out.println("User 생성 : " + user.getUserId() + " 이름 : " + user.getUsername());
		System.out.println();

		//조회(단건)
		System.out.println("===== User 하나만 조회 =====");
		Optional<UserFindResponse> byId = userService.findByUserId(new UserFindRequest(user.getUserId()));

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
		String updateName = "수정되는 이름";
		String updateEmail = "update@email.com";
		String updatePassword = "updatePassword";

		//수정 시작
		try {
			Thread.sleep(1000);		//텀을 두기위해 Sleep 설정
			userService.updateUser(
				new UserUpdateRequest(user.getUserId(), updateName, updateEmail, updatePassword
					, null, null, null, null));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		//수정된 데이터 조회
		System.out.println("===== User 수정 조회=====");
		System.out.println(user);
		System.out.println();

		//수정 후 전체 데이터 조회도
		System.out.println("===== User 수정후 전체 조회=====");
		List<UserFindResponse> modifyUsers = userService.findAll();
		modifyUsers.forEach(u -> System.out.println(u.toString()));
		System.out.println();

		//삭제
		System.out.println("===== User 삭제=====");
		System.out.println("삭제할 UUID : " + user.getUserId());
		userService.deleteUser(user.getUserId());
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== User 삭제 조회=====");
		List<UserFindResponse> deleteUsers = userService.findAll();
		deleteUsers.forEach(u -> System.out.println(u.toString()));
		System.out.println();

	}

	private static void channelCRUDTest(ChannelService channelService, UserService userService) {
		//등록
		System.out.println("===== Channel 생성 =====");
		Channel channel = channelService.createPublicChannel(new PublicChannelCreateRequest("새로운 방", "새로운 방입니다."));
		System.out.println(channel.toString());
		System.out.println();

		//조회(단건)
		System.out.println("===== Channel 조회 (단건) =====");        //정확히 단건은 아니지만 List사용을 해보고싶었음.
		Optional<ChannelFindResponse> byChannelId = channelService.findByChannelId(channel.getChannelId());
		System.out.println(byChannelId.toString());
		System.out.println();

		//조회(다건)
		System.out.println("===== Channel 전부 조회 =====");

		// 임의로 유저를 불러들었음.
		UserFindResponse userFindResponse = userService.findAll()
			.stream()
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다."));

		List<ChannelFindResponse> byAllChannel = channelService.findAllByUserId(userFindResponse.getUserId());
		System.out.println(byAllChannel.stream().toList());

		//수정
		System.out.println("===== Channel 수정 =====");
		System.out.println("수정되는 Channel ID : " + channel.getChannelId());
		String channelNameModify = "수정된 방 이름";
		channelService.updateChannel(new ChannelUpdateRequest(channel.getChannelId(), channelNameModify, "수정된 내용"));
		System.out.println();

		//수정된 데이터 조회
		System.out.println("===== 수정된 데이터 조회 =====");
		Optional<ChannelFindResponse> modifyChannelFind = channelService.findByChannelId(channel.getChannelId());
		System.out.println("수정된 데이터 조회 : " + modifyChannelFind);
		System.out.println();

		//수정 후 전체 데이터 조회도
		System.out.println("===== Channel 전체 데이터 조회 =====");
		channelService.findAllByUserId(userFindResponse.getUserId()).forEach(ch -> System.out.println(ch.toString()));
		System.out.println();

		//삭제
		System.out.println("===== Channel 삭제 =====");
		System.out.println("삭제될 데이터 ID : " + channel.getChannelId());
		channelService.deleteChannel(channel.getChannelId());
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== Channel 삭제 확인 =====");
		channelService.findAllByUserId(userFindResponse.getUserId()).forEach(ch -> System.out.println(ch.toString()));
		System.out.println();

	}

	private static void messageCRUDTest(User user1, Channel channel1, MessageService messageService) {

		//등록
		System.out.println("===== Message 생성 =====");


		// 임의로 셋업한 User를
		Message message = messageService.createMessage(new MessageCreateRequest(user1.getUserId(), channel1.getChannelId(), "첫번째 메시지",null));
		System.out.println(message.toString());
		System.out.println();

		//조회(단건)
		System.out.println("===== Message 하나만 조회 =====");
		Optional<Message> foundMessage = messageService.findByMessageId(message.getMessageId());
		foundMessage.ifPresent(m -> System.out.println("조회된 Message: " + m.toString()));
		System.out.println();

		//조회(다건)
		System.out.println("===== Message 전부 조회 =====");
		List<Message> allMessages = messageService.findAllByChannelId(channel1.getChannelId());
		allMessages.forEach(m -> System.out.println(m.toString()));
		System.out.println();

		//수정
		System.out.println("===== Message 수정 =====");
		String newContent = "수정된 첫번째 메시지";
		messageService.updateMessage(new MessageUpdateRequest(message.getMessageId(), newContent));
		System.out.println();

		//수정된 데이터 조회
		System.out.println("===== Message 수정 조회=====");
		Optional<Message> updatedMessage = messageService.findByMessageId(message.getMessageId());
		updatedMessage.ifPresent(m -> System.out.println("수정된 Message : " + m));
		System.out.println();

		//수정 후 전체 데이터 조회도
		System.out.println("===== Message 수정후 전체 조회=====");
		List<Message> messages = messageService.findAllByChannelId(channel1.getChannelId());
		messages.forEach(m -> System.out.println(m.toString()));

		//삭제
		System.out.println("===== Message 삭제=====");
		messageService.deleteMessage(message.getMessageId());
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== Message 삭제 조회=====");
		List<Message> afterDeleteMessages = messageService.findAllByChannelId(channel1.getChannelId());
		afterDeleteMessages.forEach(m -> System.out.println(m.toString()));
		System.out.println();

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
}
