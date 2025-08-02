package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.userstatus.USCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.USUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusService {
	UserStatus createUserStatus(USCreateRequest request);

	Optional<UserStatus> findById(UUID userStatusId);

	List<UserStatus> findAll();

	UserStatus updateUserStatus(USUpdateRequest request);

	UserStatus updateByUserid(UUID userId);

	void delete(UUID userStatusId);
}
