package com.sprint.mission.discodeit.repository;

import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentRepository {
	void save(BinaryContent content);
	void deleteByOwnerId(UUID ownerId);
}
