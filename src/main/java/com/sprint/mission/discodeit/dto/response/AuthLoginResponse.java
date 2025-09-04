package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import lombok.Builder;

@Builder
public record AuthLoginResponse(
    UUID id,
    String username,
    String email,
    BinaryContentDTO profile,
    Boolean online
) {

}