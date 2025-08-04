package com.sprint.mission.discodeit.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Message;

public interface MessageRepository {
	Message save(Message message);

	Optional<Message> findById(UUID messageId);

	List<Message> findAll();

	Instant LatestMessageByChannelId(UUID channelId);

	void delete(UUID id);

	void deleteByChannelId(UUID channelId);

	boolean existsById(UUID messageId);
}
