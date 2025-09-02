package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ChannelDTO(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UserDTO> participants,
    Instant lastMessageAt
) {

}
