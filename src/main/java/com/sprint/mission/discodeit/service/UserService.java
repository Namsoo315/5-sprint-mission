package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserFindRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

public interface UserService {
	User createUser(UserCreateRequest userCreateRequest, BinaryContentDTO binaryContentDTO);

	UserDto findByUserId(UserFindRequest request);

	List<UserDto> findAll();

	void updateUser(UserUpdateRequest userUpdateRequest, BinaryContentDTO binaryContentDTO);

	void deleteUser(UUID userId);
}
