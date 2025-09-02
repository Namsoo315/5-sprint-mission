package com.sprint.mission.discodeit.dto.data;

import java.util.UUID;
import lombok.Builder;

@Builder
public record BinaryContentDTO(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

}