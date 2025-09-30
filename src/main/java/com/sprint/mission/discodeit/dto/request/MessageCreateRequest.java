package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MessageCreateRequest(
    UUID authorId,
    UUID channelId,

    @NotBlank
    @Size(max = 100)
    String content
) {

}
