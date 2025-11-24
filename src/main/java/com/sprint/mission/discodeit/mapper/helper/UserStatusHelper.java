package com.sprint.mission.discodeit.mapper.helper;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.jwt.JwtRegistry;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStatusHelper {

  private final JwtRegistry<UUID> jwtRegistry;

  @Named("getOnlineStatus")
  public Boolean getOnlineStatus(User user) {
    return !jwtRegistry.hasActiveJwtInformationByUserId(user.getId());
  }
}