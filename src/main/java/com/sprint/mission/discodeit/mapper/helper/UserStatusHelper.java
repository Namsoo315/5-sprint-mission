package com.sprint.mission.discodeit.mapper.helper;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserStatusHelper {

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Named("getOnlineStatus")
  public Boolean getOnlineStatus(User user) {
    return userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);
  }
}