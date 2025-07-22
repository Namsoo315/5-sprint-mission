package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Message;

public interface MessageService {
	Message createMessage(UUID userId, UUID channelId, String content);

	List<Message> findByUserIdAndChannelId(UUID userId, UUID channelId);

	Optional<Message> findByMessage(UUID messageId, UUID userId, UUID channelId);

	List<Message> findByAllMessage();

	void updateMessage(UUID messageId, UUID userId, UUID channelId, String newContent);

	void deleteMessage(UUID messageId, UUID userId, UUID channelId);

}
