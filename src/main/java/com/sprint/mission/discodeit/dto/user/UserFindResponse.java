package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

import lombok.Builder;

@Builder
public record UserFindResponse(
	UUID userId,
	String username,
	String email,
	boolean status
) {}
