package com.sprint.mission.discodeit.dto.data;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDTO(
    UUID id,
    String username,
    String email,
    BinaryContentDTO profile,
    Boolean online
) {

}
