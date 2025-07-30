package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

@Service("messageService")
public class BasicMessageService implements MessageService {
	private final MessageRepository repo;

	public BasicMessageService(MessageRepository repo) {
		this.repo = repo;
	}

	@Override
	public Message createMessage(UUID userId, UUID channelId, String content) {

		if (userId == null) {
			throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
		}
		if (channelId == null) {
			throw new IllegalArgumentException("채널방을 찾을 수 없습니다.");
		}

		Message message = new Message(userId, channelId, content);
		repo.save(message);

		return message;
	}

	@Override
	public List<Message> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		List<Message> result = new ArrayList<>();
		for (Message m : repo.findAll()) {
			if (m.getUserId().equals(userId) && m.getChannelId().equals(channelId)) {
				result.add(m);
			}
		}

		return result;
	}

	@Override
	public Optional<Message> findByMessageId(UUID messageId) {
		return repo.findById(messageId);
	}

	public List<Message> findAll() {
		return repo.findAll();
	}

	@Override
	public void updateMessage(UUID messageId, String newContent) {
		Message message = repo.findById(messageId).orElse(null);

		if(message == null) {
			throw new IllegalArgumentException("메시지를 찾을 수 없습니다.");
		}
		message.update(newContent);
		repo.save(message);
	}

	@Override
	public void deleteMessage(UUID messageId) {
		repo.delete(messageId);
	}
}
