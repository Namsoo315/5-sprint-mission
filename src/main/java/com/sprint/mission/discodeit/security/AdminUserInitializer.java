package com.sprint.mission.discodeit.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminUserInitializer implements ApplicationRunner {

  private final InitAdminService initService;

  @Override
  public void run(ApplicationArguments args) {
    initService.initAdmin();        // 애플리케이션 실행 시 ADMIN 권한을 가진 어드민 계정이 초기화
  }
}