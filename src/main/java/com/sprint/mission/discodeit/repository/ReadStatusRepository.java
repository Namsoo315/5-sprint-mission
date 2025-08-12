package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.ReadStatus;

public interface ReadStatusRepository {
	ReadStatus save(ReadStatus readStatus);

	Optional<ReadStatus> findByReadStatusId(UUID readStatusId);

	Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

	List<ReadStatus> findAll();

	List<ReadStatus> findAllByUserId(UUID userId);

	List<ReadStatus> findAllByChannelId(UUID channelId);

	void delete(UUID readStatusId);

	void deleteByChannelId(UUID channelId);

	boolean existsById(UUID readStatusId);
}
