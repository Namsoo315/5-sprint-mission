package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.ChannelType;

import lombok.Builder;

@Builder
public record ChannelFindResponse(
    UUID id,
    ChannelType type,
    String name,
    String description,
    Instant lastMessageAt,
    List<UUID> participantIds
) {

}
