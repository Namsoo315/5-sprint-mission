package com.sprint.mission.discodeit.dto.binary;

public record BinaryCreateRequest(
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {

}
