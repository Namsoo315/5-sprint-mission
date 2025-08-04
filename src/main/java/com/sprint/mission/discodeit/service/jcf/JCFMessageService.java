package com.sprint.mission.discodeit.service.jcf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class JCFMessageService implements MessageService {
	private final MessageRepository messageRepository;
	private final UserService userService;
	private final ChannelService channelService;

	public JCFMessageService(MessageRepository messageRepository, UserService userService,
		ChannelService channelService) {
		this.messageRepository = messageRepository;
		this.userService = userService;
		this.channelService = channelService;
	}

	@Override
	public Message createMessage(UUID userId, UUID channelId, String content) {
		Optional<User> user = userService.findByUserId(userId);
		Optional<Channel> channel = channelService.findByChannelId(channelId);

		if (user.isEmpty()) {
			throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
		}
		if (channel.isEmpty()) {
			throw new IllegalArgumentException("채널방을 찾을 수 없습니다.");
		}

		Message message = new Message(userId, channelId, content);
		messageRepository.save(message);

		return message;
	}

	@Override
	public List<Message> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		List<Message> result = new ArrayList<>();
		for (Message m : messageRepository.findAll()) {
			if (m.getUserId().equals(userId) && m.getChannelId().equals(channelId)) {
				result.add(m);
			}
		}

		return result;
	}

	@Override
	public Optional<Message> findByMessageId(UUID messageId) {
		return messageRepository.findById(messageId);
	}

	public List<Message> findAll() {
		return messageRepository.findAll();
	}

	@Override
	public void updateMessage(UUID messageId, String newContent) {
		Message message = messageRepository.findById(messageId).orElse(null);

		if(message == null) {
			throw new IllegalArgumentException("메시지를 찾을 수 없습니다.");
		}
		message.update(newContent);
		messageRepository.save(message);
	}

	@Override
	public void deleteMessage(UUID messageId) {
		messageRepository.delete(messageId);
	}
}
