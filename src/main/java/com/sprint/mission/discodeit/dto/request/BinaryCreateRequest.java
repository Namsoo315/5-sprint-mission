package com.sprint.mission.discodeit.dto.request;

public record BinaryCreateRequest(
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {

}
