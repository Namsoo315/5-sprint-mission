package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto createUser(UserCreateRequest userCreateRequest, MultipartFile profile) throws IOException;

  UserDto findByUserId(UUID userId);

  List<UserDto> findAll();

  UserDto updateUser(UUID userId, UserUpdateRequest userUpdateRequest,
      MultipartFile profile) throws IOException;

  void deleteUser(UUID userId);
}
