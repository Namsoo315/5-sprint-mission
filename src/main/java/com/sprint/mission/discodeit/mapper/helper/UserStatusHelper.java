package com.sprint.mission.discodeit.mapper.helper;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.SessionManager;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStatusHelper {

  private final SessionManager sessionManager;

  @Named("getOnlineStatus")
  public Boolean getOnlineStatus(User user) {
    // Session이 비어져있지 않을 경우에 online 치부한다.
    return !sessionManager.getActiveSessionsByUserId(user.getId()).isEmpty();
  }
}