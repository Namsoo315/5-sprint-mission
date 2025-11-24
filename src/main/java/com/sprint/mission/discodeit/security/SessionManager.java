package com.sprint.mission.discodeit.security;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
// Customizing Session Manager
public class SessionManager {

  private final SessionRegistry sessionRegistry;

  // userId로 저장된 세션들을 찾아오는 메서드
  public List<SessionInformation> getActiveSessionsByUserId(UUID userId) {
    return sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof DiscodeitUserDetails)
        .map(DiscodeitUserDetails.class::cast)
        .filter(details -> details.getUserDTO() != null && userId.equals(details.getUserDTO().id()))
        .flatMap(details -> sessionRegistry.getAllSessions(details, false).stream())
        .toList();
  }

  // 세션 무효화 기능
  public void invalidateSessionsByUserId(UUID userId) {
    List<SessionInformation> activeSessions = getActiveSessionsByUserId(userId);
    if (!activeSessions.isEmpty()) {
      activeSessions.forEach(SessionInformation::expireNow);
      log.info("Session expired: {}", activeSessions.size());
    }
  }
}
