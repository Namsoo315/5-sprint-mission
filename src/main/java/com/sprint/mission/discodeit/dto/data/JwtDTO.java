package com.sprint.mission.discodeit.dto.data;

public record JwtDTO(
    UserDTO userDto,    // 프론트에 맞춰서 바꿔야함.
    String accessToken
) {

}
