package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDTO createUser(UserCreateRequest userCreateRequest, MultipartFile profile) throws IOException;

  UserDTO findByUserId(UUID userId);

  List<UserDTO> findAll();

  UserDTO updateUser(UUID userId, UserUpdateRequest userUpdateRequest,
      MultipartFile profile) throws IOException;

  void deleteUser(UUID userId);
}
