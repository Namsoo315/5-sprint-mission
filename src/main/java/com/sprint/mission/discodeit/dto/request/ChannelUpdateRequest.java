package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Size;

public record ChannelUpdateRequest(
    @Size(
        min = 3,
        max = 20,
        message = "채널 이름은 최소 3자에서 최대 20자까지 가능합니다."
    )
    String newName,

    @Size(
        min = 8,
        max = 20,
        message = "채널 설명은 최소 8자에서 최대 20자까지 가능합니다."
    )
    String newDescription
) {

}
