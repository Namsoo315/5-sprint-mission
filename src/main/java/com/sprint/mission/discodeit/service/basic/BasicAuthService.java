package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.AuthLoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserDTO login(AuthLoginRequest request) {

    // 1-1. username과 일치하는 유저가 있는지 확인
    User user = userRepository.findByUsername(request.username()).orElseThrow(
        () -> new NoSuchElementException("아이디가 일치하지 않습니다."));

    // 1-2. password과 일치하는지 확인
    if (!user.getPassword().equals(request.password())) {
      throw new NoSuchElementException("비밀번호가 일치하지 않습니다.");
    }

    // 2. DTO를 통한 username, email 보내줌.
    return userMapper.toDto(user);
  }
}
