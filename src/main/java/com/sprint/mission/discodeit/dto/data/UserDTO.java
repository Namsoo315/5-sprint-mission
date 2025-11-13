package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.UserRole;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDTO(
    UUID id,
    String username,
    String email,
    UserRole role,
    BinaryContentDTO profile,
    Boolean online
) {

}
