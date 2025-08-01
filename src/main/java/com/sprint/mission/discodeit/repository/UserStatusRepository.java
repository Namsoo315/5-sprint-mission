package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusRepository {
	UserStatus save(UserStatus userStatus);

	Optional<UserStatus> findById(UUID userStatusId);

	Optional<UserStatus> findByUserId(UUID userId);

	List<UserStatus> findAll();

	long count();

	void delete(UUID userStatusId);

	void deleteByUserId(UUID userId);

	boolean existsById(UUID userStatusId);
}
