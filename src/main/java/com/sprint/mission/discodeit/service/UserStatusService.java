package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusService {
	UserStatus createUserStatus(UserStatusCreateRequest request);

	Optional<UserStatus> findById(UUID userStatusId);

	List<UserStatus> findAll();

	UserStatus updateUserStatus(UserStatusUpdateRequest request);

	UserStatus updateByUserId(UUID userId);

	void delete(UUID userStatusId);
}
