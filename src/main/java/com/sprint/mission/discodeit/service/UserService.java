package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

  User createUser(UserCreateRequest userCreateRequest, BinaryContentDTO binaryContentDTO);

  UserDto findByUserId(UUID userId);

  List<UserDto> findAll();

  User updateUser(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentDTO binaryContentDTO);

  void deleteUser(UUID userId);
}
