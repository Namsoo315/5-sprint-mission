package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequestByUserId;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequestByUserStatusId;
import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusService {
	UserStatus createUserStatus(UserStatusCreateRequest request);

	UserStatus findByUserStatusId(UUID userStatusId);

	List<UserStatus> findAll();

	void updateUserStatus(UserStatusUpdateRequestByUserStatusId request);

	void updateByUserId(UserStatusUpdateRequestByUserId request);

	void delete(UUID userStatusId);
}
