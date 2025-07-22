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
	private final List<Message> list = new ArrayList<>();
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
		list.add(message);
		return message;
	}

	@Override
	public List<Message> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		List<Message> result = new ArrayList<>();
		for (Message m : list) {
			if (m.getUserId().equals(userId) && m.getChannelId().equals(channelId)) {
				result.add(m);
			}
		}

		return result;
	}

	@Override
	public Optional<Message> findByMessage(UUID messageId, UUID userId, UUID channelId) {
		return list.stream()
			.filter(m -> m.getMessageId().equals(messageId)
				&& m.getUserId().equals(userId)
				&& m.getChannelId().equals(channelId))
			.findFirst();
	}

	public List<Message> findByAllMessage() {
		return List.copyOf(list);
	}

	@Override
	public void updateMessage(UUID messageId, UUID userId, UUID channelId, String newContent) {
		findByMessage(messageId, userId, channelId)
			.ifPresent(m -> m.update(newContent));
	}

	@Override
	public void deleteMessage(UUID messageId, UUID userId, UUID channelId) {
		list.removeIf(m -> m.getMessageId().equals(messageId)
			&& m.getUserId().equals(userId)
			&& m.getChannelId().equals(channelId));
	}
}
