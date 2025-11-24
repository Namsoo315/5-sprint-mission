package com.sprint.mission.discodeit.dto.data;

public record JwtDTO(
    UserDTO userDTO,
    String accessToken
) {

}
