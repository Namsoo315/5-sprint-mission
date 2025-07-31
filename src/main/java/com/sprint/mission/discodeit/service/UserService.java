package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserFindRequest;
import com.sprint.mission.discodeit.dto.UserFindResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

public interface UserService {
	User createUser(UserCreateRequest request);

	Optional<UserFindResponse> findByUserId(UserFindRequest request);

	List<UserFindResponse> findAll();

	void updateUser(UserUpdateRequest request);

	void deleteUser(UUID uuid);
}
