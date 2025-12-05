package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

// 새로운 메시지가 등록되면 이벤트를 발행하세요.
public record MessageCreatedEvent(
    UUID userId,
    UUID channelId,
    String content,
    List<ReadStatus> enabledUsers
) {

}
