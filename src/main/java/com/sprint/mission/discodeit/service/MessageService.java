package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Message;

public interface MessageService {
	Message createMessage(UUID userId, UUID channelId, String content);

	List<Message> findByUserIdAndChannelId(UUID userId, UUID channelId);

	Optional<Message> findByUserChannelAndContent(UUID userId, UUID channelId, String content);

	List<Message> findByAllMessage();

	void updateMessage(UUID userId, UUID channelId, String oldContent, String newContent);

	void deleteMessage(UUID userId, UUID channelId, String content);

}
