package com.sprint.mission.discodeit.mapper.helper;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStatusHelper {

  private final UserStatusRepository userStatusRepository;

  @Named("getOnlineStatus")
  public Boolean getOnlineStatus(User user) {
    return userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);
  }
}