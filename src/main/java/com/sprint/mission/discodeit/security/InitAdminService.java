package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class InitAdminService {


  @Value("${discodeit.admin.username}")
  private String adminUsername;
  @Value("${discodeit.admin.password}")
  private String adminPassword;
  @Value("${discodeit.admin.email}")
  private String adminEmail;

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void initAdmin() {
    if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(
        adminUsername)) {
      log.warn("이미 관리자가 존재합니다.");
      return;
    }

    User admin = User.builder()
        .username(adminUsername)
        .email(adminEmail)
        .password(passwordEncoder.encode(adminPassword))
        .role(UserRole.ADMIN)
        .build();

    userRepository.save(admin);
    log.info("관리자가 초기화되었습니다.");
  }

}
