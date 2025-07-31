package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.ReadStatus;

public interface ReadStatusRepository {
	void save(ReadStatus status);

	List<UUID> findUserIdsByChannelId(UUID channelId);
}
