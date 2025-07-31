package com.sprint.mission.discodeit.repository;

import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusRepository {
	void save(UserStatus status);

	Optional<UserStatus> findById(UUID statusId);

	void deleteByUserId(UUID userId);
}
