package com.sprint.mission.discodeit.service.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class JCFMessageService implements MessageService {
	private final Map<UUID, Message> map = new HashMap<>();
	private final UserService userService;
	private final ChannelService channelService;

	public JCFMessageService(UserService userService, ChannelService channelService) {
		this.userService = userService;
		this.channelService = channelService;
	}

	@Override
	public Message createMessage(UUID userId, UUID channelId, String content) {
		Optional<User> user = userService.findById(userId);
		Optional<Channel> channel = channelService.findById(channelId);

		if (user.isEmpty()) {
			throw new IllegalArgumentException("user id not found");
		}
		if (channel.isEmpty()) {
			throw new IllegalArgumentException("channel id not found");
		}

		Message message = new Message(userId, channelId, content);
		map.put(message.getMessageId(), message);
		return message;
	}

	@Override
	public List<Message> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		List<Message> result = new ArrayList<>();
		for (Message m : map.values()) {
			if (m.getUserId().equals(userId) && m.getChannelId().equals(channelId)) {
				result.add(m);
			}
		}

		return result;
	}

	@Override
	public Optional<Message> findByMessage(UUID messageId) {
		return Optional.ofNullable(map.get(messageId));
	}

	public List<Message> findByAllMessage() {
		return List.copyOf(map.values());
	}

	@Override
	public void updateMessage(UUID messageId, String newContent) {
		map.get(messageId).setMessage(newContent);
		System.out.println(messageId + " 업데이트 완료 : " + newContent );
	}

	@Override
	public void deleteMessage(UUID messageId) {
		Message remove = map.remove(messageId);
		System.out.println("메시지 ID : " + remove.getMessageId() + " 메시지 삭제 완료 : "+ remove.getMessage());
	}
}
