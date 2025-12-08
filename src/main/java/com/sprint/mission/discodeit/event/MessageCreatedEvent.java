package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.ReadStatusDTO;
import java.util.List;
import java.util.UUID;

// 새로운 메시지가 등록되면 이벤트를 발행하세요.
public record MessageCreatedEvent(
    UUID userId,
    UUID channelId,
    String username,
    String content,
    List<ReadStatusDTO> enabledUsers
) {

}
