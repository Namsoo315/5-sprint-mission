package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.AuthLoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidUserCredentialsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserDTO login(AuthLoginRequest request) {

    // 1-1. username과 일치하는 유저가 있는지 확인
    User user = userRepository.findByUsername(request.username()).orElseThrow(
        () -> InvalidUserCredentialsException.wrongUsername(request.username()));

    // 1-2. password과 일치하는지 확인
    if (!user.getPassword().equals(request.password())) {
      throw InvalidUserCredentialsException.wrongPassword(request.password());
    }

    // 2. DTO를 통한 username, email 보내줌.
    return userMapper.toDto(user);
  }
}
