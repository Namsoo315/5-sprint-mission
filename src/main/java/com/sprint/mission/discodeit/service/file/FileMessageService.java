package com.sprint.mission.discodeit.service.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

public class FileMessageService implements MessageService{
	private final MessageRepository repo = new FileMessageRepository();

	@Override
	public Message createMessage(UUID userId, UUID channelId, String content) {
		Message message = new Message(channelId, userId, content);
		repo.save(message);
		return message;
	}
	@Override
	public List<Message> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		List<Message> list = new ArrayList<>();

		for(Message message : repo.findAll()) {
			if(message.getUserId().equals(userId)
				&& message.getChannelId().equals(channelId)) {
				list.add(message);
			}
		}

		return list;
	}
	@Override
	public Optional<Message> findByMessage(UUID messageId) {
		return repo.findById(messageId);
	}

	@Override
	public List<Message> findByAllMessage() {
		return repo.findAll();
	}

	@Override
	public void updateMessage(UUID messageId, String newContent) {
		Message message = repo.findById(messageId).orElse(null);

		if(message == null) {
			throw new IllegalArgumentException("메시지가 존재하지 않습니다.");
		}

		message.setMessage(newContent);
		repo.save(message);
	}

	@Override
	public void deleteMessage(UUID messageId) {
		repo.delete(messageId);
	}
}
