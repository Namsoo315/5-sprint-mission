package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import java.time.Instant;

public record JwtObject(
    Instant issueTime,
    Instant expirationTime,
    UserDTO userDTO,
    String token
) {

  public boolean isExpired() {
    return expirationTime.isBefore(Instant.now());
  }
}
