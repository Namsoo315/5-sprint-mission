package com.sprint.mission.discodeit.event;

import java.util.UUID;

// BinaryContent 메타 정보가 DB에 잘 저장되었다는 사실을 의미하는 이벤트입니다.
public record BinaryContentCreatedEvent(
    UUID id,
    byte[] bytes
) {

}
