package com.sprint.mission.discodeit.service.basic;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  public AuthLoginResponse login(AuthLoginRequest request) {
    Optional<User> optionalUser = userRepository.findByUsername(request.username());

    // 1-1. username과 일치하는 유저가 있는지 확인
    if (optionalUser.isEmpty()) {
      throw new IllegalArgumentException("존재하지 않는 회원입니다.");
    }
    User user = optionalUser.get();

    // 1-2. password과 일치하는지 확인
    if (!user.getPassword().equals(request.password())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    // 2. DTO를 통한 username, email 보내줌.
    return AuthLoginResponse.builder()
        .id(user.getUserId())
        .username(user.getUsername())
        .password(user.getPassword())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .profileId(user.getProfileId())
        .build();
  }
}
