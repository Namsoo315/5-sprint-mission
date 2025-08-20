package com.sprint.mission.discodeit.dto.auth;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record AuthLoginResponse(
    UUID id,
    String username,
    String email,
    String password,
    Instant createdAt,
    Instant updatedAt,
    UUID profileId
) {

}