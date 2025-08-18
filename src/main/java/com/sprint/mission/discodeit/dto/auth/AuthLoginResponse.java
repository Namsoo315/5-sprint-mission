package com.sprint.mission.discodeit.dto.auth;

import lombok.Builder;

@Builder
public record AuthLoginResponse(
	String username,
	String email
) {}